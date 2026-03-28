package com.hnh.service.inventory;

import com.hnh.dto.order.OrderVariantRequest;
import com.hnh.dto.order.OrderVariantResponse;
import com.hnh.entity.order.OrderVariantKey;
import com.hnh.service.CrudService;

public interface OrderVariantService extends CrudService<OrderVariantKey, OrderVariantRequest, OrderVariantResponse> {}

