package com.smartshop.dto.response;

import java.util.UUID;

public class WishlistCheckResponse {

    private UUID userId;
    private UUID productId;
    private boolean inWishlist;

    public WishlistCheckResponse() {
    }

    public WishlistCheckResponse(UUID userId, UUID productId, boolean inWishlist) {
        this.userId = userId;
        this.productId = productId;
        this.inWishlist = inWishlist;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public boolean isInWishlist() {
        return inWishlist;
    }

    public void setInWishlist(boolean inWishlist) {
        this.inWishlist = inWishlist;
    }
}
