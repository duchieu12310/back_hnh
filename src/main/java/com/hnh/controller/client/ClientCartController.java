package com.hnh.controller.client;

import com.hnh.constant.AppConstants;
import com.hnh.constant.FieldName;
import com.hnh.constant.ResourceName;
import com.hnh.dto.client.ClientCartRequest;
import com.hnh.dto.client.ClientCartResponse;
import com.hnh.dto.client.ClientCartVariantKeyRequest;
import com.hnh.entity.cart.Cart;
import com.hnh.entity.cart.CartVariant;
import com.hnh.entity.cart.CartVariantKey;
import com.hnh.exception.ResourceNotFoundException;
import com.hnh.mapper.client.ClientCartMapper;
import com.hnh.repository.cart.CartRepository;
import com.hnh.repository.cart.CartVariantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/client-api/carts")
@AllArgsConstructor
@CrossOrigin(AppConstants.FRONTEND_HOST)
public class ClientCartController {

    private final CartRepository cartRepository;
    private final CartVariantRepository cartVariantRepository;
    private final ClientCartMapper clientCartMapper;
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<ObjectNode> getCart(Authentication authentication) {
        String username = authentication.getName();

        List<Cart> carts = cartRepository.findByUsername(username);
        if (carts.size() > 1) {
            for (int i = 0; i < carts.size() - 1; i++) {
                Cart oldCart = carts.get(i);
                oldCart.setStatus(0);
                cartRepository.save(oldCart);
            }
        }
        java.util.Optional<Cart> activeCart = carts.isEmpty() ? java.util.Optional.empty() : java.util.Optional.of(carts.get(carts.size() - 1));

        // Reference: https://stackoverflow.com/a/11828920, https://stackoverflow.com/a/51456293
        ObjectNode response = activeCart
                .map(clientCartMapper::entityToResponse)
                .map(clientCartResponse -> objectMapper.convertValue(clientCartResponse, ObjectNode.class))
                .orElse(objectMapper.createObjectNode());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<ClientCartResponse> saveCart(@RequestBody ClientCartRequest request, Authentication authentication) {
        final Cart cartBeforeSave;

        // Xử lý tạo mới nếu request.getCartId() == null nhưng user có thể đã có giỏ hàng
        if (request.getCartId() == null) {
            List<Cart> carts = cartRepository.findByUsername(authentication.getName());
            if (carts.size() > 1) {
                for (int i = 0; i < carts.size() - 1; i++) {
                    Cart oldCart = carts.get(i);
                    oldCart.setStatus(0);
                    cartRepository.save(oldCart);
                }
            }
            java.util.Optional<Cart> activeCart = carts.isEmpty() ? java.util.Optional.empty() : java.util.Optional.of(carts.get(carts.size() - 1));

            cartBeforeSave = activeCart
                    .map(existingEntity -> clientCartMapper.partialUpdate(existingEntity, request))
                    .orElseGet(() -> clientCartMapper.requestToEntity(request));
        } else {
            cartBeforeSave = cartRepository.findById(request.getCartId())
                    .map(existingEntity -> clientCartMapper.partialUpdate(existingEntity, request))
                    .orElseThrow(() -> new ResourceNotFoundException(ResourceName.CART, FieldName.ID, request.getCartId()));
        }

        // Validate Variant Inventory
        for (CartVariant cartVariant : cartBeforeSave.getCartVariants()) {
            Integer inventory = cartVariant.getVariant() != null ? cartVariant.getVariant().getQuantity() : 0;
            if (inventory == null) inventory = 0;
            
            if (cartVariant.getQuantity() > inventory) {
                throw new RuntimeException("Variant quantity cannot greater than variant inventory");
            }
        }

        Cart cart = cartRepository.save(cartBeforeSave);
        ClientCartResponse clientCartResponse = clientCartMapper.entityToResponse(cart);
        return ResponseEntity.status(HttpStatus.OK).body(clientCartResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCartItems(@RequestBody List<ClientCartVariantKeyRequest> idRequests) {
        List<CartVariantKey> ids = idRequests.stream()
                .map(idRequest -> new CartVariantKey(idRequest.getCartId(), idRequest.getVariantId()))
                .collect(Collectors.toList());
        cartVariantRepository.deleteAllById(ids);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

