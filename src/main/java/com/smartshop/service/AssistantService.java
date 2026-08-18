package com.smartshop.service;

import com.smartshop.ai.RequirementExtractor;
import com.smartshop.dto.common.InterpretedRequirements;
import com.smartshop.dto.request.AssistantSearchRequest;
import com.smartshop.dto.response.AssistantSearchResponse;
import com.smartshop.dto.response.RankedProductResponse;
import com.smartshop.entity.Product;
import com.smartshop.recommendation.RecommendationEngine;
import com.smartshop.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AssistantService {

    private static final Logger log = LoggerFactory.getLogger(AssistantService.class);

    private final RequirementExtractor requirementExtractor;
    private final ProductRepository productRepository;
    private final RecommendationEngine recommendationEngine;

    public AssistantService(RequirementExtractor requirementExtractor,
                            ProductRepository productRepository,
                            RecommendationEngine recommendationEngine) {
        this.requirementExtractor = requirementExtractor;
        this.productRepository = productRepository;
        this.recommendationEngine = recommendationEngine;
    }

    public AssistantSearchResponse processSearch(AssistantSearchRequest request) {
        String query = request.getQuery();
        log.info("Processing shopping assistant query: '{}'", query);

        // Step 1: Extract structured shopping requirements (Gemini with heuristic fallback)
        InterpretedRequirements reqs = requirementExtractor.extractRequirements(query);

        // Step 2: Fetch candidate products from PostgreSQL
        List<Product> candidateProducts = findCandidateProducts(reqs);

        // Step 3: Deterministically rank products and generate transparent reasons
        List<RankedProductResponse> rankedProducts = recommendationEngine.rankProducts(candidateProducts, reqs);

        log.info("Assistant search complete. Found {} ranked products for query: '{}'",
                rankedProducts.size(), query);

        return new AssistantSearchResponse(true, query, reqs, rankedProducts);
    }

    private List<Product> findCandidateProducts(InterpretedRequirements reqs) {
        if (reqs == null || reqs.getCategory() == null || reqs.getCategory().isBlank() || "all".equalsIgnoreCase(reqs.getCategory())) {
            return productRepository.findAll();
        }

        String category = reqs.getCategory().trim();
        List<Product> candidates = productRepository.findByCategoryContainingIgnoreCase(category);

        // If no products match the extracted category keyword, fallback to full catalog to find closest matches
        if (candidates.isEmpty()) {
            log.info("No direct category matches for '{}', evaluating entire product catalog for closest match", category);
            candidates = productRepository.findAll();
        }

        return candidates;
    }
}
