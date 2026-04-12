package com.hnh.service.warehouse;

import com.hnh.dto.warehouse.StorageLocationRequest;
import com.hnh.dto.warehouse.StorageLocationResponse;
import com.hnh.service.CrudService;

public interface StorageLocationService extends CrudService<Long, StorageLocationRequest, StorageLocationResponse> {
    StorageLocationResponse adjustQuantity(Long id, Integer quantity);
    StorageLocationResponse clearQuantity(Long id);
}
