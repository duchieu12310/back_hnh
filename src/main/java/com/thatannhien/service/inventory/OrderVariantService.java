package com.thatannhien.service.inventory;

import com.thatannhien.dto.order.OrderVariantRequest;
import com.thatannhien.dto.order.OrderVariantResponse;
import com.thatannhien.entity.order.OrderVariantKey;
import com.thatannhien.service.CrudService;

public interface OrderVariantService extends CrudService<OrderVariantKey, OrderVariantRequest, OrderVariantResponse> {}
