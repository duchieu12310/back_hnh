package com.hnh.service.promotion;

import com.hnh.dto.promotion.PromotionRequest;
import com.hnh.dto.promotion.PromotionResponse;
import com.hnh.service.CrudService;

import java.time.Instant;

public interface PromotionService extends CrudService<Long, PromotionRequest, PromotionResponse> {

    boolean checkCanCreatePromotionForProduct(Long productId, Instant startDate, Instant endDate);

}

