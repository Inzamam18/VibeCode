package com.smartshop.recommendation;

import com.smartshop.dto.common.InterpretedRequirements;
import com.smartshop.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SuitabilityScorerTest {

    private SuitabilityScorer scorer;

    @BeforeEach
    void setUp() {
        scorer = new SuitabilityScorer();
    }

    @Test
    @DisplayName("Product within budget with matching priorities should receive high suitability score")
    void testMatchingProductScore() {
        Product product = new Product();
        product.setName("OnePlus Nord 3 5G");
        product.setBrand("OnePlus");
        product.setCategory("smartphone");
        product.setPrice(BigDecimal.valueOf(28999));
        product.setDiscount(BigDecimal.valueOf(10));
        product.setRating(BigDecimal.valueOf(4.5));
        product.setReviewCount(350);
        product.setSpecifications("50MP Sony IMX890 OIS camera, 5000mAh battery, 80W SuperVOOC, 16GB RAM");
        product.setFeatures("50MP camera with OIS, 5000mAh battery, 120Hz Fluid AMOLED display");
        product.setPerformance("Dimensity 9000 flagship level processor");
        product.setPros("Superb primary camera, great battery backup, fast charging");
        product.setCons("No headphone jack");

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

        double score = scorer.calculateSuitabilityScore(product, reqs);
        assertTrue(score >= 80.0 && score <= 100.0, "Score should be between 80 and 100, got: " + score);
    }

    @Test
    @DisplayName("Product way above budget should receive penalized score without crashing")
    void testOverBudgetPenalty() {
        Product expensiveProduct = new Product();
        expensiveProduct.setName("Samsung Galaxy S24 Ultra");
        expensiveProduct.setBrand("Samsung");
        expensiveProduct.setCategory("smartphone");
        expensiveProduct.setPrice(BigDecimal.valueOf(129999));
        expensiveProduct.setRating(BigDecimal.valueOf(4.8));
        expensiveProduct.setReviewCount(500);

        InterpretedRequirements reqs = new InterpretedRequirements(
                "smartphone",
                null,
                BigDecimal.valueOf(30000),
                List.of(),
                List.of(),
                List.of("camera"),
                List.of(),
                null
        );

        double score = scorer.calculateSuitabilityScore(expensiveProduct, reqs);
        assertTrue(score < 50.0, "Score should be heavily penalized for being 4x over budget, got: " + score);
        assertTrue(score >= 0.0, "Score must never be negative");
    }

    @Test
    @DisplayName("Score must never be less than 0 or greater than 100")
    void testScoreBounds() {
        Product product = new Product();
        product.setName("Cheap Item");
        product.setPrice(BigDecimal.valueOf(1000));
        product.setRating(BigDecimal.valueOf(1.0));

        InterpretedRequirements extremeReqs = new InterpretedRequirements(
                "smartphone",
                null,
                BigDecimal.valueOf(1), // ₹1 budget
                List.of(),
                List.of("some_brand"),
                List.of("unmatched_priority"),
                List.of("unmatched_feature"),
                null
        );

        double score = scorer.calculateSuitabilityScore(product, extremeReqs);
        assertTrue(score >= 0.0 && score <= 100.0, "Score must stay bounded [0, 100], got: " + score);
    }
}
