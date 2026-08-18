package com.smartshop.ai;

import com.smartshop.dto.common.InterpretedRequirements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class FallbackRequirementExtractorTest {

    private FallbackRequirementExtractor extractor;

    @BeforeEach
    void setUp() {
        extractor = new FallbackRequirementExtractor();
    }

    @Test
    @DisplayName("TEST 1: I need a phone under ₹30,000 with a great camera and battery.")
    void testShoppingQuery1() {
        String query = "I need a phone under ₹30,000 with a great camera and battery.";
        InterpretedRequirements reqs = extractor.extract(query);

        assertEquals("smartphone", reqs.getCategory());
        assertNotNull(reqs.getMaxPrice());
        assertEquals(30000, reqs.getMaxPrice().intValue());
        assertTrue(reqs.getPriorities().contains("camera"), "Should include camera priority");
        assertTrue(reqs.getPriorities().contains("battery"), "Should include battery priority");
    }

    @Test
    @DisplayName("TEST 2: I need a laptop for coding under ₹70,000.")
    void testShoppingQuery2() {
        String query = "I need a laptop for coding under ₹70,000.";
        InterpretedRequirements reqs = extractor.extract(query);

        assertEquals("laptop", reqs.getCategory());
        assertNotNull(reqs.getMaxPrice());
        assertEquals(70000, reqs.getMaxPrice().intValue());
        assertEquals("coding", reqs.getUseCase());
        assertTrue(reqs.getPriorities().contains("coding"));
    }

    @Test
    @DisplayName("TEST 3: I want wireless headphones with long battery life.")
    void testShoppingQuery3() {
        String query = "I want wireless headphones with long battery life.";
        InterpretedRequirements reqs = extractor.extract(query);

        assertEquals("headphones", reqs.getCategory());
        assertTrue(reqs.getPriorities().contains("battery"));
        assertTrue(reqs.getRequiredFeatures().contains("wireless"));
    }

    @Test
    @DisplayName("TEST 4: Ambiguous query 'Suggest a good phone.' should not crash")
    void testShoppingQuery4Ambiguous() {
        String query = "Suggest a good phone.";
        InterpretedRequirements reqs = extractor.extract(query);

        assertEquals("smartphone", reqs.getCategory());
        assertNull(reqs.getMaxPrice());
    }

    @Test
    @DisplayName("TEST 5: Extreme low budget 'I need a phone for ₹1.'")
    void testShoppingQuery5ExtremeBudget() {
        String query = "I need a phone for ₹1.";
        InterpretedRequirements reqs = extractor.extract(query);

        assertEquals("smartphone", reqs.getCategory());
        assertNotNull(reqs.getMaxPrice());
        assertEquals(1, reqs.getMaxPrice().intValue());
    }
}
