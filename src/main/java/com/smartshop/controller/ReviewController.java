package com.smartshop.controller;

import com.smartshop.dto.request.CreateReviewRequest;
import com.smartshop.dto.response.ReviewResponse;
import com.smartshop.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products/{productId}/reviews")
@Tag(name = "Reviews", description = "Product review retrieval and submission APIs")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    @Operation(summary = "Get all reviews for a product",
            description = "Returns all customer reviews for the specified product ordered by newest first.")
    public ResponseEntity<List<ReviewResponse>> getReviewsForProduct(
            @Parameter(description = "Product UUID")
            @PathVariable UUID productId
    ) {
        List<ReviewResponse> reviews = reviewService.getReviewsForProduct(productId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    @Operation(summary = "Submit a new review for a product",
            description = "Validates rating between 1 and 5 and submits a review for the specified product.")
    public ResponseEntity<ReviewResponse> createReview(
            @Parameter(description = "Product UUID")
            @PathVariable UUID productId,
            @Valid @RequestBody CreateReviewRequest request
    ) {
        ReviewResponse created = reviewService.createReview(productId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
