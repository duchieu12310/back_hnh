package com.thatannhien.service.order;

import com.thatannhien.config.payment.paypal.PayPalHttpClient;
import com.thatannhien.constant.AppConstants;
import com.thatannhien.constant.FieldName;
import com.thatannhien.constant.ResourceName;
import com.thatannhien.dto.client.ClientConfirmedOrderResponse;
import com.thatannhien.dto.client.ClientSimpleOrderRequest;
import com.thatannhien.dto.payment.OrderIntent;
import com.thatannhien.dto.payment.OrderStatus;
import com.thatannhien.dto.payment.PaymentLandingPage;
import com.thatannhien.dto.payment.PaypalRequest;
import com.thatannhien.dto.payment.PaypalResponse;
import com.thatannhien.dto.waybill.GhnCancelOrderRequest;
import com.thatannhien.dto.waybill.GhnCancelOrderResponse;
import com.thatannhien.entity.authentication.User;
import com.thatannhien.entity.cart.Cart;
import com.thatannhien.entity.cashbook.PaymentMethodType;
import com.thatannhien.entity.general.Notification;
import com.thatannhien.entity.general.NotificationType;
import com.thatannhien.entity.order.Order;
import com.thatannhien.entity.order.OrderResource;
import com.thatannhien.entity.order.OrderVariant;
import com.thatannhien.entity.promotion.Promotion;
import com.thatannhien.entity.waybill.Waybill;
import com.thatannhien.entity.waybill.WaybillLog;
import com.thatannhien.exception.ResourceNotFoundException;
import com.thatannhien.mapper.client.ClientOrderMapper;
import com.thatannhien.mapper.general.NotificationMapper;
import com.thatannhien.repository.authentication.UserRepository;
import com.thatannhien.repository.cart.CartRepository;
import com.thatannhien.repository.general.NotificationRepository;
import com.thatannhien.repository.order.OrderRepository;
import com.thatannhien.repository.promotion.PromotionRepository;
import com.thatannhien.repository.waybill.WaybillLogRepository;
import com.thatannhien.repository.waybill.WaybillRepository;
import com.thatannhien.repository.inventory.DocketVariantRepository;
import com.thatannhien.repository.inventory.DocketRepository;
import com.thatannhien.repository.inventory.DocketReasonRepository;
import com.thatannhien.repository.inventory.WarehouseRepository;
import com.thatannhien.entity.inventory.Docket;
import com.thatannhien.entity.inventory.DocketReason;
import com.thatannhien.entity.inventory.DocketVariant;
import com.thatannhien.entity.inventory.DocketVariantKey;
import com.thatannhien.entity.inventory.Warehouse;
import com.thatannhien.service.general.NotificationService;
import com.thatannhien.utils.VNpayService;
import com.thatannhien.utils.InventoryUtils;
import com.thatannhien.entity.cart.CartVariant;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Value("${app.shipping.ghnToken}")
    private String ghnToken;
    @Value("${app.shipping.ghnShopId}")
    private String ghnShopId;
    @Value("${app.shipping.ghnApiPath}")
    private String ghnApiPath;
    @Value("${app.inventory.default-warehouse-id:1}")
    private Long defaultWarehouseId;
    @Value("${app.inventory.default-export-docket-reason-id:1}")
    private Long defaultExportDocketReasonId;

    private final VNpayService vNpayService;

    private final OrderRepository orderRepository;
    private final WaybillRepository waybillRepository;
    private final WaybillLogRepository waybillLogRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PromotionRepository promotionRepository;
    private final DocketVariantRepository docketVariantRepository;
    private final DocketRepository docketRepository;
    private final WarehouseRepository warehouseRepository;
    private final DocketReasonRepository docketReasonRepository;

    private final PayPalHttpClient payPalHttpClient;
    private final ClientOrderMapper clientOrderMapper;

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    private static final int USD_VND_RATE = 25_000;

    @Override
    public void cancelOrder(String code) {
        Order order = orderRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceName.ORDER, FieldName.ORDER_CODE, code));

        // Hủy đơn hàng khi status = 1 hoặc 2
        if (order.getStatus() < 3) {
            order.setStatus(5); // Status 5 là trạng thái Hủy
            orderRepository.save(order);

            // Xóa phiếu xuất kho để hoàn lại tồn kho
            docketRepository.findByOrder_IdAndType(order.getId(), 2)
                    .ifPresent(docket -> {
                        // Xóa phiếu xuất kho (sẽ tự động xóa cả docket_variant do cascade)
                        docketRepository.delete(docket);
                        log.info("Deleted export docket {} for cancelled order {}", docket.getCode(), order.getCode());
                    });

            Waybill waybill = waybillRepository.findByOrderId(order.getId()).orElse(null);

            // Status 1 là Vận đơn đang chờ lấy hàng
            if (waybill != null && waybill.getStatus() == 1) {
                String cancelOrderApiPath = ghnApiPath + "/switch-status/cancel";

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("Token", ghnToken);
                headers.add("ShopId", ghnShopId);

                RestTemplate restTemplate = new RestTemplate();

                var request = new HttpEntity<>(new GhnCancelOrderRequest(List.of(waybill.getCode())), headers);
                var response = restTemplate.postForEntity(cancelOrderApiPath, request, GhnCancelOrderResponse.class);

                if (response.getStatusCode() != HttpStatus.OK) {
                    throw new RuntimeException("Error when calling Cancel Order GHN API");
                }

                // Tích hợp Api GHN
                if (response.getBody() != null) {
                    for (var data : response.getBody().getData()) {
                        if (data.getResult()) {
                            WaybillLog waybillLog = new WaybillLog();
                            waybillLog.setWaybill(waybill);
                            waybillLog.setPreviousStatus(waybill.getStatus()); // Status 1: Đang đợi lấy hàng
                            waybillLog.setCurrentStatus(4);
                            waybillLogRepository.save(waybillLog);

                            waybill.setStatus(4); // Status 4 là trạng thái Hủy
                            waybillRepository.save(waybill);
                        }
                    }
                }
            }
        } else {
            throw new RuntimeException(String
                    .format("Order with code %s is in delivery or has been cancelled. Please check again!", code));
        }
    }

    @Override
    public ClientConfirmedOrderResponse createClientOrder(ClientSimpleOrderRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));

        Cart cart = cartRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceName.CART, FieldName.USERNAME, username));

        // (0) Validate Variant Inventory - Kiểm tra tồn kho trước khi tạo đơn hàng
        for (CartVariant cartVariant : cart.getCartVariants()) {
            int inventory = InventoryUtils
                    .calculateInventoryIndices(docketVariantRepository.findByVariantId(cartVariant.getCartVariantKey().getVariantId()))
                    .get("canBeSold");
            if (cartVariant.getQuantity() > inventory) {
                throw new RuntimeException(
                        String.format("Sản phẩm %s không đủ số lượng trong kho. Số lượng có thể bán: %d, số lượng yêu cầu: %d",
                                cartVariant.getVariant().getSku(), inventory, cartVariant.getQuantity())
                );
            }
        }

        // (1) Tạo đơn hàng
        Order order = new Order();

        order.setCode(RandomString.make(12).toUpperCase());
        order.setStatus(1); // Status 1: Đơn hàng mới
        order.setToName(user.getFullname());
        order.setToPhone(user.getPhone());
        order.setToAddress(user.getAddress().getLine());
        order.setToWardName(user.getAddress().getWard().getName());
        order.setToDistrictName(user.getAddress().getDistrict().getName());
        order.setToProvinceName(user.getAddress().getProvince().getName());
        order.setOrderResource((OrderResource) new OrderResource().setId(1L)); // Default OrderResource
        order.setUser(user);

        order.setOrderVariants(cart.getCartVariants().stream()
                .map(cartVariant -> {
                    Promotion promotion = promotionRepository
                            .findActivePromotionByProductId(cartVariant.getVariant().getProduct().getId())
                            .stream()
                            .findFirst()
                            .orElse(null);

                    double currentPrice = calculateDiscountedPrice(cartVariant.getVariant().getPrice(),
                            promotion == null ? 0 : promotion.getPercent());

                    return new OrderVariant()
                            .setOrder(order)
                            .setVariant(cartVariant.getVariant())
                            .setPrice(BigDecimal.valueOf(currentPrice))
                            .setQuantity(cartVariant.getQuantity())
                            .setAmount(BigDecimal.valueOf(currentPrice).multiply(BigDecimal.valueOf(cartVariant.getQuantity())));
                })
                .collect(Collectors.toSet()));

        // Calculate price values
        // TODO: Vấn đề khuyến mãi
        BigDecimal totalAmount = BigDecimal.valueOf(order.getOrderVariants().stream()
                .mapToDouble(orderVariant -> orderVariant.getAmount().doubleValue())
                .sum());

        BigDecimal tax = BigDecimal.valueOf(AppConstants.DEFAULT_TAX);

        BigDecimal shippingCost = BigDecimal.ZERO;

        BigDecimal totalPay = totalAmount
                .add(totalAmount.multiply(tax).setScale(0, RoundingMode.HALF_UP))
                .add(shippingCost);

        order.setTotalAmount(totalAmount);
        order.setTax(tax);
        order.setShippingCost(shippingCost);
        order.setTotalPay(totalPay);
        order.setPaymentMethodType(request.getPaymentMethodType());
        order.setPaymentStatus(1); // Status 1: Chưa thanh toán

        // (2) Tạo response
        ClientConfirmedOrderResponse response = new ClientConfirmedOrderResponse();

        response.setOrderCode(order.getCode());
        response.setOrderPaymentMethodType(order.getPaymentMethodType());

        // (3) Kiểm tra hình thức thanh toán
        if (request.getPaymentMethodType() == PaymentMethodType.CASH) {
            orderRepository.save(order);
        } else if (request.getPaymentMethodType() == PaymentMethodType.PAYPAL) {
            try {
                // (3.2.1) Tính tổng tiền theo USD
                BigDecimal totalPayUSD = order.getTotalPay()
                        .divide(BigDecimal.valueOf(USD_VND_RATE), 0, RoundingMode.HALF_UP);

                // (3.2.2) Tạo một yêu cầu giao dịch PayPal
                PaypalRequest paypalRequest = new PaypalRequest();

                paypalRequest.setIntent(OrderIntent.CAPTURE);
                paypalRequest.setPurchaseUnits(List.of(
                        new PaypalRequest.PurchaseUnit(
                                new PaypalRequest.PurchaseUnit.Money("USD", totalPayUSD.toString())
                        )
                ));

                paypalRequest.setApplicationContext(new PaypalRequest.PayPalAppContext()
                        .setBrandName("Phuc Anh Duong")
                        .setLandingPage(PaymentLandingPage.BILLING)
                        .setReturnUrl(AppConstants.BACKEND_HOST + "/client-api/orders/success")
                        .setCancelUrl(AppConstants.BACKEND_HOST + "/client-api/orders/cancel"));

                PaypalResponse paypalResponse = payPalHttpClient.createPaypalTransaction(paypalRequest);

                // (3.2.3) Lưu order
                order.setPaypalOrderId(paypalResponse.getId());
                order.setPaypalOrderStatus(paypalResponse.getStatus().toString());

                orderRepository.save(order);

                // (3.2.4) Trả về đường dẫn checkout cho user
                for (PaypalResponse.Link link : paypalResponse.getLinks()) {
                    if ("approve".equals(link.getRel())) {
                        response.setOrderPaypalCheckoutLink(link.getHref());
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Cannot create PayPal transaction request!" + e);
            }
        } else if (request.getPaymentMethodType() == PaymentMethodType.VNPAY) {
            // Convert BigDecimal to long (VNPay requires amount in VND, will be converted to cents)
            long totalPayVND = order.getTotalPay().longValue();
            var url = vNpayService.getPayUrl(order.getCode(), totalPayVND, null);

            if (StringUtils.isBlank(url)) {
                throw new RuntimeException("Cannot create VNPay transaction request!");
            }
            order.setPaypalOrderId(order.getCode());
            orderRepository.save(order);
            response.setOrderPaypalCheckoutLink(url);
        } else {
            throw new RuntimeException("Cannot identify payment method");
        }

        // (4) Vô hiệu cart
        cart.setStatus(2); // Status 2: Vô hiệu lực
        cartRepository.save(cart);

        // (5) Tạo phiếu xuất kho ngay khi đặt hàng (status = 1) để trừ tồn kho
        createExportDocketForOrder(order);

        return response;
    }

    @Override
    public void captureTransactionPaypal(String paypalOrderId, String payerId) {
        Order order = orderRepository.findByPaypalOrderId(paypalOrderId)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceName.ORDER, FieldName.PAYPAL_ORDER_ID, paypalOrderId));

        order.setPaypalOrderStatus(OrderStatus.APPROVED.toString());

        try {
            // (1) Capture
            payPalHttpClient.capturePaypalTransaction(paypalOrderId, payerId);

            // (2) Cập nhật order
            order.setPaypalOrderStatus(OrderStatus.COMPLETED.toString());
            order.setPaymentStatus(2); // Status 2: Đã thanh toán

            // (3) Gửi notification
            Notification notification = new Notification()
                    .setUser(order.getUser())
                    .setType(NotificationType.CHECKOUT_PAYPAL_SUCCESS)
                    .setMessage(String.format("Đơn hàng %s của bạn đã được thanh toán thành công bằng PayPal.", order.getCode()))
                    .setAnchor("/order/detail/" + order.getCode())
                    .setStatus(1);

            notificationRepository.save(notification);

            notificationService.pushNotification(order.getUser().getUsername(),
                    notificationMapper.entityToResponse(notification));
        } catch (Exception e) {
            log.error("Cannot capture transaction: {0}", e);
        }

        orderRepository.save(order);
    }

    @Override
    public void captureTransactionVNPay(String vnpayOrderId, boolean isSuccess) {
        Order order = orderRepository.findByCode(vnpayOrderId)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceName.ORDER, FieldName.ORDER_CODE, vnpayOrderId));
        if (isSuccess) {
            order.setPaypalOrderStatus(OrderStatus.COMPLETED.toString());
            order.setPaymentStatus(2); // Status 2: Đã thanh toán
            orderRepository.save(order);
            Notification notification = new Notification()
                    .setUser(order.getUser())
                    .setType(NotificationType.CHECKOUT_PAYPAL_SUCCESS)
                    .setMessage(String.format("Đơn hàng %s của bạn đã được thanh toán thành công bằng VNPay.", order.getCode()))
                    .setAnchor("/order/detail/" + order.getCode())
                    .setStatus(1);
            notificationRepository.save(notification);

            notificationService.pushNotification(order.getUser().getUsername(),
                    notificationMapper.entityToResponse(notification));
        } else {
            Notification notification = new Notification()
                    .setUser(order.getUser())
                    .setType(NotificationType.CHECKOUT_PAYPAL_CANCEL)
                    .setMessage(String.format("Bạn đã hủy thanh toán VNPay cho đơn hàng %s.", order.getCode()))
                    .setAnchor("/order/detail/" + order.getCode())
                    .setStatus(1);

            notificationRepository.save(notification);

            notificationService.pushNotification(order.getUser().getUsername(),
                    notificationMapper.entityToResponse(notification));
        }

    }

    private Double calculateDiscountedPrice(Double price, Integer discount) {
        return price * (100 - discount) / 100;
    }

    /**
     * Tạo phiếu xuất kho (docket type = 2, status = 1) ngay khi đặt hàng để trừ tồn kho.
     * Khi đơn hàng giao thành công, phiếu xuất sẽ được cập nhật lên status = 3.
     */
    private void createExportDocketForOrder(Order order) {
        // Đơn hàng không có chi tiết thì bỏ qua
        if (order.getOrderVariants() == null || order.getOrderVariants().isEmpty()) {
            return;
        }

        // Nếu đã có docket xuất (type = 2) gắn với đơn này rồi thì không tạo lại (idempotent)
        if (docketRepository.existsByOrder_IdAndType(order.getId(), 2)) {
            return;
        }

        // Lấy kho và lý do xuất kho mặc định
        Warehouse warehouse = warehouseRepository.findById(defaultWarehouseId)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceName.WAREHOUSE, FieldName.ID, defaultWarehouseId));

        DocketReason docketReason = docketReasonRepository.findById(defaultExportDocketReasonId)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceName.DOCKET_REASON, FieldName.ID, defaultExportDocketReasonId));

        // (1) Tạo phiếu xuất kho với status = 1 (Mới)
        Docket docket = new Docket();
        docket.setType(2); // 2 = Phiếu Xuất
        docket.setCode("EXP-" + order.getCode()); // Code duy nhất dựa trên mã đơn hàng
        docket.setReason(docketReason);
        docket.setWarehouse(warehouse);
        docket.setOrder(order);
        docket.setStatus(1); // Status 1: Mới (sẽ được cập nhật lên 3 khi giao thành công)

        Docket savedDocket = docketRepository.save(docket);

        // (2) Tạo chi tiết phiếu xuất từ các order_variant
        List<DocketVariant> docketVariants = new ArrayList<>();

        for (OrderVariant orderVariant : order.getOrderVariants()) {
            DocketVariantKey key = new DocketVariantKey(savedDocket.getId(), orderVariant.getVariant().getId());

            DocketVariant docketVariant = new DocketVariant();
            docketVariant.setDocketVariantKey(key);
            docketVariant.setDocket(savedDocket);
            docketVariant.setVariant(orderVariant.getVariant());
            docketVariant.setQuantity(orderVariant.getQuantity());

            docketVariants.add(docketVariant);
        }

        docketVariantRepository.saveAll(docketVariants);
    }

}
