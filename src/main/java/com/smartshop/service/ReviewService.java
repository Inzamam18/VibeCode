package com.smartshop.service;

import com.smartshop.dto.request.CreateReviewRequest;
import com.smartshop.dto.response.ReviewResponse;
import com.smartshop.entity.Product;
import com.smartshop.entity.Review;
import com.smartshop.exception.ResourceNotFoundException;
import com.smartshop.mapper.ReviewMapper;
import com.smartshop.repository.ProductRepository;
import com.smartshop.repository.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ReviewService {

    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final ReviewMapper reviewMapper;

    public ReviewService(ReviewRepository reviewRepository,
                         ProductRepository productRepository,
                         ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.reviewMapper = reviewMapper;
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsForProduct(UUID productId) {
        log.info("Fetching reviews for product {}", productId);
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        List<Review> reviews = reviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
        return reviewMapper.toResponseList(reviews);
    }

    public ReviewResponse createReview(UUID productId, CreateReviewRequest request) {
        log.info("Creating review for product {} by user {}", productId, request.getUserId());

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        Review review = new Review(
                productId,
                request.getUserId(),
                request.getRating(),
                request.getTitle(),
                request.getComment()
        );

        Review savedReview = reviewRepository.save(review);

        // Optionally recalculate product rating and count
        List<Review> allReviews = reviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
        double avgRating = allReviews.stream().mapToInt(Review::getRating).average().orElse(request.getRating());
        product.setRating(BigDecimal.valueOf(avgRating).setScale(2, RoundingMode.HALF_UP));
        product.setReviewCount(allReviews.size());
        productRepository.save(product);

        return reviewMapper.toResponse(savedReview);
    }
}
