package com.hnh.service.inventory;

import com.hnh.dto.inventory.CountVariantRequest;
import com.hnh.dto.inventory.CountVariantResponse;
import com.hnh.entity.inventory.CountVariantKey;
import com.hnh.service.CrudService;

public interface CountVariantService extends CrudService<CountVariantKey, CountVariantRequest, CountVariantResponse> {}

