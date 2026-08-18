package com.smartshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AssistantSearchRequest {

    @NotBlank(message = "Query must not be blank")
    @Size(max = 500, message = "Query must not exceed 500 characters")
    private String query;

    public AssistantSearchRequest() {
    }

    public AssistantSearchRequest(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
