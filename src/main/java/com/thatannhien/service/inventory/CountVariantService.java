package com.thatannhien.service.inventory;

import com.thatannhien.dto.inventory.CountVariantRequest;
import com.thatannhien.dto.inventory.CountVariantResponse;
import com.thatannhien.entity.inventory.CountVariantKey;
import com.thatannhien.service.CrudService;

public interface CountVariantService extends CrudService<CountVariantKey, CountVariantRequest, CountVariantResponse> {}
