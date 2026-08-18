package com.smartshop.controller;

import com.smartshop.dto.request.AssistantSearchRequest;
import com.smartshop.dto.response.AssistantSearchResponse;
import com.smartshop.service.AssistantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assistant")
@Tag(name = "AI Assistant", description = "AI Shopping Assistant endpoints for natural language shopping queries")
public class AssistantController {

    private final AssistantService assistantService;

    public AssistantController(AssistantService assistantService) {
        this.assistantService = assistantService;
    }

    @PostMapping("/search")
    @Operation(summary = "Search products using AI natural language query",
            description = "Extracts shopping requirements using Gemini AI, searches PostgreSQL, deterministically ranks products, and returns ranked recommendations with suitability scores and factual reasons.")
    public ResponseEntity<AssistantSearchResponse> search(@Valid @RequestBody AssistantSearchRequest request) {
        AssistantSearchResponse response = assistantService.processSearch(request);
        return ResponseEntity.ok(response);
    }
}
