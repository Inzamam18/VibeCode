package com.smartshop.dto.response;

import com.smartshop.dto.common.InterpretedRequirements;
import java.util.ArrayList;
import java.util.List;

public class AssistantSearchResponse {

    private Boolean success;
    private String originalQuery;
    private InterpretedRequirements interpretedRequirements;
    private List<RankedProductResponse> rankedProducts = new ArrayList<>();

    public AssistantSearchResponse() {
    }

    public AssistantSearchResponse(Boolean success, String originalQuery,
                                  InterpretedRequirements interpretedRequirements,
                                  List<RankedProductResponse> rankedProducts) {
        this.success = success;
        this.originalQuery = originalQuery;
        this.interpretedRequirements = interpretedRequirements;
        if (rankedProducts != null) this.rankedProducts = rankedProducts;
    }

    // Getters and Setters
    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getOriginalQuery() {
        return originalQuery;
    }

    public void setOriginalQuery(String originalQuery) {
        this.originalQuery = originalQuery;
    }

    public InterpretedRequirements getInterpretedRequirements() {
        return interpretedRequirements;
    }

    public void setInterpretedRequirements(InterpretedRequirements interpretedRequirements) {
        this.interpretedRequirements = interpretedRequirements;
    }

    public List<RankedProductResponse> getRankedProducts() {
        return rankedProducts;
    }

    public void setRankedProducts(List<RankedProductResponse> rankedProducts) {
        this.rankedProducts = rankedProducts != null ? rankedProducts : new ArrayList<>();
    }
}
