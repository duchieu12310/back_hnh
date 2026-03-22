package com.thatannhien.service.inventory;

import com.thatannhien.dto.inventory.DocketRequest;
import com.thatannhien.dto.inventory.DocketResponse;
import com.thatannhien.service.CrudService;

public interface DocketService extends CrudService<Long, DocketRequest, DocketResponse> {}
