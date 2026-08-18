package com.smartshop.service;

import com.smartshop.dto.request.WishlistAddRequest;
import com.smartshop.dto.response.ProductResponse;
import com.smartshop.dto.response.WishlistCheckResponse;
import com.smartshop.entity.WishlistItem;
import com.smartshop.exception.DuplicateResourceException;
import com.smartshop.exception.ResourceNotFoundException;
import com.smartshop.mapper.WishlistMapper;
import com.smartshop.repository.ProductRepository;
import com.smartshop.repository.WishlistItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class WishlistService {

    private static final Logger log = LoggerFactory.getLogger(WishlistService.class);

    private final WishlistItemRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final WishlistMapper wishlistMapper;

    public WishlistService(WishlistItemRepository wishlistRepository,
                           ProductRepository productRepository,
                           WishlistMapper wishlistMapper) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
        this.wishlistMapper = wishlistMapper;
    }

    public void addToWishlist(WishlistAddRequest request) {
        UUID userId = request.getUserId();
        UUID productId = request.getProductId();

        log.info("Adding product {} to wishlist for user {}", productId, userId);

        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        if (wishlistRepository.existsByUserIdAndProductId(userId, productId)) {
            log.warn("Product {} is already in wishlist for user {}", productId, userId);
            throw new DuplicateResourceException("Product is already in wishlist");
        }

        WishlistItem item = new WishlistItem(userId, productId);
        wishlistRepository.save(item);
    }

    public void removeFromWishlist(UUID userId, UUID productId) {
        log.info("Removing product {} from wishlist for user {}", productId, userId);
        if (userId != null) {
            wishlistRepository.deleteByUserIdAndProductId(userId, productId);
        } else {
            wishlistRepository.deleteByProductId(productId);
        }
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getWishlist(UUID userId) {
        log.info("Fetching wishlist items for user {}", userId);
        List<WishlistItem> items = wishlistRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return wishlistMapper.toProductResponseList(items);
    }

    @Transactional(readOnly = true)
    public WishlistCheckResponse checkWishlist(UUID userId, UUID productId) {
        boolean inWishlist = wishlistRepository.existsByUserIdAndProductId(userId, productId);
        return new WishlistCheckResponse(userId, productId, inWishlist);
    }
}
