package com.smartshop.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InterpretedRequirements {

    private String category;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<String> preferredBrands = new ArrayList<>();
    private List<String> excludedBrands = new ArrayList<>();
    private List<String> priorities = new ArrayList<>();
    private List<String> requiredFeatures = new ArrayList<>();
    private String useCase;

    public InterpretedRequirements() {
    }

    public InterpretedRequirements(String category, BigDecimal minPrice, BigDecimal maxPrice,
                                  List<String> preferredBrands, List<String> excludedBrands,
                                  List<String> priorities, List<String> requiredFeatures, String useCase) {
        this.category = category;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        if (preferredBrands != null) this.preferredBrands = preferredBrands;
        if (excludedBrands != null) this.excludedBrands = excludedBrands;
        if (priorities != null) this.priorities = priorities;
        if (requiredFeatures != null) this.requiredFeatures = requiredFeatures;
        this.useCase = useCase;
    }

    // Getters and Setters
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public List<String> getPreferredBrands() {
        return preferredBrands;
    }

    public void setPreferredBrands(List<String> preferredBrands) {
        this.preferredBrands = preferredBrands != null ? preferredBrands : new ArrayList<>();
    }

    public List<String> getExcludedBrands() {
        return excludedBrands;
    }

    public void setExcludedBrands(List<String> excludedBrands) {
        this.excludedBrands = excludedBrands != null ? excludedBrands : new ArrayList<>();
    }

    public List<String> getPriorities() {
        return priorities;
    }

    public void setPriorities(List<String> priorities) {
        this.priorities = priorities != null ? priorities : new ArrayList<>();
    }

    public List<String> getRequiredFeatures() {
        return requiredFeatures;
    }

    public void setRequiredFeatures(List<String> requiredFeatures) {
        this.requiredFeatures = requiredFeatures != null ? requiredFeatures : new ArrayList<>();
    }

    public String getUseCase() {
        return useCase;
    }

    public void setUseCase(String useCase) {
        this.useCase = useCase;
    }
}
