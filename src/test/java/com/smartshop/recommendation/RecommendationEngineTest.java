package com.smartshop.recommendation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartshop.dto.common.InterpretedRequirements;
import com.smartshop.dto.response.RankedProductResponse;
import com.smartshop.entity.Product;
import com.smartshop.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RecommendationEngineTest {

    private RecommendationEngine recommendationEngine;
    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapper(new ObjectMapper());
        SuitabilityScorer scorer = new SuitabilityScorer();
        ExplanationGenerator generator = new ExplanationGenerator();
        recommendationEngine = new RecommendationEngine(scorer, generator, productMapper);
    }

    @Test
    @DisplayName("Rank products correctly based on camera priority and budget constraint")
    void testRankingOrdersCorrectly() {
        Product phoneA = new Product();
        phoneA.setId(UUID.randomUUID());
        phoneA.setName("Phone A - Camera Focused");
        phoneA.setBrand("OnePlus");
        phoneA.setCategory("smartphone");
        phoneA.setPrice(BigDecimal.valueOf(28000));
        phoneA.setRating(BigDecimal.valueOf(4.6));
        phoneA.setReviewCount(120);
        phoneA.setSpecifications("50MP Sony IMX890 Camera, 5000mAh Battery");
        phoneA.setFeatures("50MP Camera, Fast 80W Charging");
        phoneA.setPros("Incredible camera shots, all-day battery");

        Product phoneB = new Product();
        phoneB.setId(UUID.randomUUID());
        phoneB.setName("Phone B - Basic Entry");
        phoneB.setBrand("BrandX");
        phoneB.setCategory("smartphone");
        phoneB.setPrice(BigDecimal.valueOf(15000));
        phoneB.setRating(BigDecimal.valueOf(3.8));
        phoneB.setReviewCount(30);
        phoneB.setSpecifications("13MP basic camera, 4000mAh battery");
        phoneB.setFeatures("Basic screen");

        InterpretedRequirements reqs = new InterpretedRequirements(
                "smartphone",
                null,
                BigDecimal.valueOf(30000),
                List.of(),
                List.of(),
                List.of("camera", "battery"),
                List.of(),
                null
        );

        List<RankedProductResponse> ranked = recommendationEngine.rankProducts(List.of(phoneB, phoneA), reqs);

        assertNotNull(ranked);
        assertEquals(2, ranked.size());
        assertEquals("Phone A - Camera Focused", ranked.get(0).getProduct().getName(),
                "Phone A should be ranked #1 due to matching camera/battery priorities and budget");
        assertTrue(ranked.get(0).getSuitabilityScore() > ranked.get(1).getSuitabilityScore());
        assertNotNull(ranked.get(0).getRecommendationReason());
        assertFalse(ranked.get(0).getStrengths().isEmpty());
    }
}
