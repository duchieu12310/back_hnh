package com.thatannhien.service.inventory;

import com.thatannhien.dto.inventory.DocketVariantRequest;
import com.thatannhien.dto.inventory.DocketVariantResponse;
import com.thatannhien.entity.inventory.DocketVariantKey;
import com.thatannhien.service.CrudService;

public interface DocketVariantService extends CrudService<DocketVariantKey, DocketVariantRequest, DocketVariantResponse> {}
