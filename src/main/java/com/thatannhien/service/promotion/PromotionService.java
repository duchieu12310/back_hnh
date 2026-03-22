package com.thatannhien.service.promotion;

import com.thatannhien.dto.promotion.PromotionRequest;
import com.thatannhien.dto.promotion.PromotionResponse;
import com.thatannhien.service.CrudService;

import java.time.Instant;

public interface PromotionService extends CrudService<Long, PromotionRequest, PromotionResponse> {

    boolean checkCanCreatePromotionForProduct(Long productId, Instant startDate, Instant endDate);

}
