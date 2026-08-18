package com.smartshop.controller;

import com.smartshop.dto.request.WishlistAddRequest;
import com.smartshop.dto.response.ApiResponse;
import com.smartshop.dto.response.ProductResponse;
import com.smartshop.dto.response.WishlistCheckResponse;
import com.smartshop.service.WishlistService;
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
@RequestMapping("/api/wishlist")
@Tag(name = "Wishlist", description = "User wishlist management APIs")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping
    @Operation(summary = "Add a product to user wishlist",
            description = "Prevents duplicate wishlist entries for the same user and product.")
    public ResponseEntity<ApiResponse<Void>> addToWishlist(@Valid @RequestBody WishlistAddRequest request) {
        wishlistService.addToWishlist(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Product added to wishlist successfully", null));
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Remove product from wishlist",
            description = "Removes a product from user's wishlist.")
    public ResponseEntity<ApiResponse<Void>> removeFromWishlist(
            @Parameter(description = "Product UUID")
            @PathVariable UUID productId,
            @Parameter(description = "User UUID (optional)")
            @RequestParam(required = false) UUID userId
    ) {
        wishlistService.removeFromWishlist(userId, productId);
        return ResponseEntity.ok(ApiResponse.ok("Product removed from wishlist successfully", null));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get all wishlist products for user",
            description = "Returns list of products in the user's wishlist.")
    public ResponseEntity<List<ProductResponse>> getWishlist(
            @Parameter(description = "User UUID")
            @PathVariable UUID userId
    ) {
        List<ProductResponse> products = wishlistService.getWishlist(userId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{userId}/check/{productId}")
    @Operation(summary = "Check if product is in user wishlist",
            description = "Returns boolean indicating if product is in wishlist.")
    public ResponseEntity<WishlistCheckResponse> checkWishlist(
            @Parameter(description = "User UUID")
            @PathVariable UUID userId,
            @Parameter(description = "Product UUID")
            @PathVariable UUID productId
    ) {
        WishlistCheckResponse response = wishlistService.checkWishlist(userId, productId);
        return ResponseEntity.ok(response);
    }
}
