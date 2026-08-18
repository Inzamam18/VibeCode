package com.smartshop.repository;

import com.smartshop.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, UUID> {

    List<WishlistItem> findByUserIdOrderByCreatedAtDesc(UUID userId);

    Optional<WishlistItem> findByUserIdAndProductId(UUID userId, UUID productId);

    boolean existsByUserIdAndProductId(UUID userId, UUID productId);

    @Transactional
    @Modifying
    @Query("DELETE FROM WishlistItem w WHERE w.userId = :userId AND w.productId = :productId")
    void deleteByUserIdAndProductId(@Param("userId") UUID userId, @Param("productId") UUID productId);

    @Transactional
    @Modifying
    @Query("DELETE FROM WishlistItem w WHERE w.productId = :productId")
    void deleteByProductId(@Param("productId") UUID productId);
}
