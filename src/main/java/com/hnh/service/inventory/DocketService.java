package com.hnh.service.inventory;

import com.hnh.dto.inventory.DocketRequest;
import com.hnh.dto.inventory.DocketResponse;
import com.hnh.service.CrudService;

public interface DocketService extends CrudService<Long, DocketRequest, DocketResponse> {}

