package com.smartshop.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {

    private UUID id;
    private String name;
    private String brand;
    private String category;
    private String description;
    private BigDecimal price;
    private BigDecimal discount;
    private String imageUrl;
    private BigDecimal rating;
    private Integer reviewCount;
    private Object specifications;
    private Object features;
    private Object performance;
    private Object pros;
    private Object cons;
    private Boolean availability;

    public ProductResponse() {
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Object getSpecifications() {
        return specifications;
    }

    public void setSpecifications(Object specifications) {
        this.specifications = specifications;
    }

    public Object getFeatures() {
        return features;
    }

    public void setFeatures(Object features) {
        this.features = features;
    }

    public Object getPerformance() {
        return performance;
    }

    public void setPerformance(Object performance) {
        this.performance = performance;
    }

    public Object getPros() {
        return pros;
    }

    public void setPros(Object pros) {
        this.pros = pros;
    }

    public Object getCons() {
        return cons;
    }

    public void setCons(Object cons) {
        this.cons = cons;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }
}
