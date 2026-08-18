package com.smartshop.mapper;

import com.smartshop.dto.response.ReviewResponse;
import com.smartshop.entity.Review;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ReviewMapper {

    public ReviewResponse toResponse(Review review) {
        if (review == null) {
            return null;
        }

        return new ReviewResponse(
                review.getId(),
                review.getProductId(),
                review.getUserId(),
                review.getRating(),
                review.getTitle(),
                review.getComment(),
                review.getCreatedAt()
        );
    }

    public List<ReviewResponse> toResponseList(List<Review> reviews) {
        if (reviews == null) {
            return Collections.emptyList();
        }
        return reviews.stream()
                .map(this::toResponse)
                .toList();
    }
}
