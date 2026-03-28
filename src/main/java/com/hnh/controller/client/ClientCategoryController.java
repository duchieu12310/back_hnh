package com.hnh.controller.client;

import com.hnh.constant.AppConstants;
import com.hnh.constant.FieldName;
import com.hnh.constant.ResourceName;
import com.hnh.dto.CollectionWrapper;
import com.hnh.dto.client.ClientCategoryResponse;
import com.hnh.entity.product.Category;
import com.hnh.exception.ResourceNotFoundException;
import com.hnh.mapper.client.ClientCategoryMapper;
import com.hnh.repository.product.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/client-api/categories")
@AllArgsConstructor
@CrossOrigin(AppConstants.FRONTEND_HOST)
public class ClientCategoryController {

    private CategoryRepository categoryRepository;
    private ClientCategoryMapper clientCategoryMapper;

    @GetMapping
    public ResponseEntity<CollectionWrapper<ClientCategoryResponse>> getAllCategories() {
        List<Category> firstCategories = categoryRepository.findByParentCategoryIsNull();
        List<ClientCategoryResponse> clientCategoryResponses = clientCategoryMapper.entityToResponse(firstCategories, 3);
        return ResponseEntity.status(HttpStatus.OK).body(CollectionWrapper.of(clientCategoryResponses));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ClientCategoryResponse> getCategory(@PathVariable("slug") String slug) {
        ClientCategoryResponse clientCategoryResponse = categoryRepository.findBySlug(slug)
                .map(category -> clientCategoryMapper.entityToResponse(category, false))
                .orElseThrow(() -> new ResourceNotFoundException(ResourceName.CATEGORY, FieldName.SLUG, slug));
        return ResponseEntity.status(HttpStatus.OK).body(clientCategoryResponse);
    }

}

