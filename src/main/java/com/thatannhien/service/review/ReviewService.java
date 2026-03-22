package com.thatannhien.service.review;

import com.thatannhien.dto.review.ReviewRequest;
import com.thatannhien.dto.review.ReviewResponse;
import com.thatannhien.service.CrudService;

public interface ReviewService extends CrudService<Long, ReviewRequest, ReviewResponse> {}
