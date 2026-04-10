package com.hnh.service.review;

import com.hnh.constant.FieldName;
import com.hnh.constant.ResourceName;
import com.hnh.constant.SearchFields;
import com.hnh.dto.ListResponse;
import com.hnh.dto.review.ReviewRequest;
import com.hnh.dto.review.ReviewResponse;
import com.hnh.entity.review.Review;
import com.hnh.exception.ResourceNotFoundException;
import com.hnh.mapper.review.ReviewMapper;
import com.hnh.repository.review.ReviewRepository;
// TODO: TẠM THỜI COMMENT - FLOW ĐIỂM THƯỞNG
// import com.hnh.utils.RewardUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private ReviewRepository reviewRepository;
    private ReviewMapper reviewMapper;
    // TODO: TẠM THỜI COMMENT - FLOW ĐIỂM THƯỞNG
    // private RewardUtils rewardUtils;

    @Override
    public ListResponse<ReviewResponse> findAll(int page, int size, String sort, String filter, String search, boolean all) {
        return defaultFindAll(page, size, sort, filter, search, all, SearchFields.REVIEW, reviewRepository, reviewMapper);
    }

    @Override
    public ReviewResponse findById(Long id) {
        return defaultFindById(id, reviewRepository, reviewMapper, ResourceName.REVIEW);
    }

    @Override
    public ReviewResponse save(ReviewRequest request) {
        return defaultSave(request, reviewRepository, reviewMapper);
    }

    @Override
    public ReviewResponse save(Long id, ReviewRequest request) {
        Review review = reviewRepository.findById(id)
                .map(existingEntity -> reviewMapper.partialUpdate(existingEntity, request))
                .map(reviewRepository::save)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceName.REVIEW, FieldName.ID, id));

        // TODO: TẠM THỜI COMMENT - FLOW ĐIỂM THƯỞNG
        // rewardUtils.approveReviewHook(review);

        return reviewMapper.entityToResponse(review);
    }

    @Override
    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public void delete(List<Long> ids) {
        reviewRepository.deleteAllById(ids);
    }

    @Override
    public ReviewResponse updateStatus(Long id, Integer status) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceName.REVIEW, FieldName.ID, id));
        review.setStatus(status);
        review = reviewRepository.save(review);
        return reviewMapper.entityToResponse(review);
    }

}

