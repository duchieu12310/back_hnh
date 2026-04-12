package com.hnh.service.review;

import com.hnh.dto.review.ReviewRequest;
import com.hnh.dto.review.ReviewResponse;
import com.hnh.service.CrudService;

public interface ReviewService extends CrudService<Long, ReviewRequest, ReviewResponse> {}

