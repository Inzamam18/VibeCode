package com.smartshop.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_preferences")
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "preferred_categories", columnDefinition = "TEXT")
    private String preferredCategories;

    @Column(name = "preferred_brands", columnDefinition = "TEXT")
    private String preferredBrands;

    @Column(name = "minimum_budget", precision = 12, scale = 2)
    private BigDecimal minimumBudget;

    @Column(name = "maximum_budget", precision = 12, scale = 2)
    private BigDecimal maximumBudget;

    @Column(name = "preferred_features", columnDefinition = "TEXT")
    private String preferredFeatures;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public UserPreference() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getPreferredCategories() {
        return preferredCategories;
    }

    public void setPreferredCategories(String preferredCategories) {
        this.preferredCategories = preferredCategories;
    }

    public String getPreferredBrands() {
        return preferredBrands;
    }

    public void setPreferredBrands(String preferredBrands) {
        this.preferredBrands = preferredBrands;
    }

    public BigDecimal getMinimumBudget() {
        return minimumBudget;
    }

    public void setMinimumBudget(BigDecimal minimumBudget) {
        this.minimumBudget = minimumBudget;
    }

    public BigDecimal getMaximumBudget() {
        return maximumBudget;
    }

    public void setMaximumBudget(BigDecimal maximumBudget) {
        this.maximumBudget = maximumBudget;
    }

    public String getPreferredFeatures() {
        return preferredFeatures;
    }

    public void setPreferredFeatures(String preferredFeatures) {
        this.preferredFeatures = preferredFeatures;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
