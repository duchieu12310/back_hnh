package com.hnh.service.inventory;

import com.hnh.dto.inventory.PurchaseOrderVariantRequest;
import com.hnh.dto.inventory.PurchaseOrderVariantResponse;
import com.hnh.entity.inventory.PurchaseOrderVariantKey;
import com.hnh.service.CrudService;

public interface PurchaseOrderVariantService extends CrudService<PurchaseOrderVariantKey, PurchaseOrderVariantRequest,
        PurchaseOrderVariantResponse> {}

