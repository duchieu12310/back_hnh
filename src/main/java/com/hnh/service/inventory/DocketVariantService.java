package com.hnh.service.inventory;

import com.hnh.dto.inventory.DocketVariantRequest;
import com.hnh.dto.inventory.DocketVariantResponse;
import com.hnh.entity.inventory.DocketVariantKey;
import com.hnh.service.CrudService;

public interface DocketVariantService extends CrudService<DocketVariantKey, DocketVariantRequest, DocketVariantResponse> {}

