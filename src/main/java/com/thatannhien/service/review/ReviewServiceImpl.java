package com.thatannhien.service.review;

import com.thatannhien.constant.FieldName;
import com.thatannhien.constant.ResourceName;
import com.thatannhien.constant.SearchFields;
import com.thatannhien.dto.ListResponse;
import com.thatannhien.dto.review.ReviewRequest;
import com.thatannhien.dto.review.ReviewResponse;
import com.thatannhien.entity.review.Review;
import com.thatannhien.exception.ResourceNotFoundException;
import com.thatannhien.mapper.review.ReviewMapper;
import com.thatannhien.repository.review.ReviewRepository;
// TODO: TẠM THỜI COMMENT - FLOW ĐIỂM THƯỞNG
// import com.thatannhien.utils.RewardUtils;
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
                .orElseThrow(() -> new ResourceNotFoundException(ResourceName.DOCKET, FieldName.ID, id));

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

}
