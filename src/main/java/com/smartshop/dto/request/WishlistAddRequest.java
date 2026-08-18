package com.smartshop.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class WishlistAddRequest {

    @NotNull(message = "userId is required")
    private UUID userId;

    @NotNull(message = "productId is required")
    private UUID productId;

    public WishlistAddRequest() {
    }

    public WishlistAddRequest(UUID userId, UUID productId) {
        this.userId = userId;
        this.productId = productId;
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
}
