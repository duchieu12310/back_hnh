package com.hnh.service.product;

import com.hnh.dto.product.ProductRequest;
import com.hnh.dto.product.ProductResponse;
import com.hnh.service.CrudService;

public interface ProductService extends CrudService<Long, ProductRequest, ProductResponse> {
}
