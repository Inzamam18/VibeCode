package com.smartshop.dto.response;

import java.util.ArrayList;
import java.util.List;

public class RankedProductResponse {

    private ProductResponse product;
    private Double suitabilityScore;
    private String recommendationReason;
    private List<String> strengths = new ArrayList<>();
    private List<String> tradeoffs = new ArrayList<>();

    public RankedProductResponse() {
    }

    public RankedProductResponse(ProductResponse product, Double suitabilityScore,
                                String recommendationReason, List<String> strengths,
                                List<String> tradeoffs) {
        this.product = product;
        this.suitabilityScore = suitabilityScore;
        this.recommendationReason = recommendationReason;
        if (strengths != null) this.strengths = strengths;
        if (tradeoffs != null) this.tradeoffs = tradeoffs;
    }

    // Getters and Setters
    public ProductResponse getProduct() {
        return product;
    }

    public void setProduct(ProductResponse product) {
        this.product = product;
    }

    public Double getSuitabilityScore() {
        return suitabilityScore;
    }

    public void setSuitabilityScore(Double suitabilityScore) {
        this.suitabilityScore = suitabilityScore;
    }

    public String getRecommendationReason() {
        return recommendationReason;
    }

    public void setRecommendationReason(String recommendationReason) {
        this.recommendationReason = recommendationReason;
    }

    public List<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<String> strengths) {
        this.strengths = strengths != null ? strengths : new ArrayList<>();
    }

    public List<String> getTradeoffs() {
        return tradeoffs;
    }

    public void setTradeoffs(List<String> tradeoffs) {
        this.tradeoffs = tradeoffs != null ? tradeoffs : new ArrayList<>();
    }
}
