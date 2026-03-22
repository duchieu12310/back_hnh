package com.thatannhien.service.inventory;

import com.thatannhien.dto.inventory.PurchaseOrderVariantRequest;
import com.thatannhien.dto.inventory.PurchaseOrderVariantResponse;
import com.thatannhien.entity.inventory.PurchaseOrderVariantKey;
import com.thatannhien.service.CrudService;

public interface PurchaseOrderVariantService extends CrudService<PurchaseOrderVariantKey, PurchaseOrderVariantRequest,
        PurchaseOrderVariantResponse> {}
