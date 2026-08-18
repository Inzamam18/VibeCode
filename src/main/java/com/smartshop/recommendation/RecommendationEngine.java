package com.smartshop.recommendation;

import com.smartshop.dto.common.InterpretedRequirements;
import com.smartshop.dto.response.ProductResponse;
import com.smartshop.dto.response.RankedProductResponse;
import com.smartshop.entity.Product;
import com.smartshop.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class RecommendationEngine {

    private static final Logger log = LoggerFactory.getLogger(RecommendationEngine.class);

    private final SuitabilityScorer suitabilityScorer;
    private final ExplanationGenerator explanationGenerator;
    private final ProductMapper productMapper;

    public RecommendationEngine(SuitabilityScorer suitabilityScorer,
                                ExplanationGenerator explanationGenerator,
                                ProductMapper productMapper) {
        this.suitabilityScorer = suitabilityScorer;
        this.explanationGenerator = explanationGenerator;
        this.productMapper = productMapper;
    }

    public List<RankedProductResponse> rankProducts(List<Product> candidateProducts, InterpretedRequirements reqs) {
        if (candidateProducts == null || candidateProducts.isEmpty()) {
            return Collections.emptyList();
        }

        log.info("Ranking {} candidate products against requirements (category: {}, maxPrice: {})",
                candidateProducts.size(),
                reqs != null ? reqs.getCategory() : "any",
                reqs != null ? reqs.getMaxPrice() : "none");

        return candidateProducts.stream()
                .map(product -> {
                    double score = suitabilityScorer.calculateSuitabilityScore(product, reqs);
                    String reason = explanationGenerator.generateRecommendationReason(product, reqs, score);
                    List<String> strengths = explanationGenerator.extractStrengths(product, reqs);
                    List<String> tradeoffs = explanationGenerator.extractTradeoffs(product, reqs);
                    ProductResponse productResponse = productMapper.toResponse(product);

                    return new RankedProductResponse(productResponse, score, reason, strengths, tradeoffs);
                })
                .sorted(Comparator.comparingDouble(RankedProductResponse::getSuitabilityScore).reversed())
                .toList();
    }
}
