package com.smartshop.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartshop.dto.common.InterpretedRequirements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeminiResponseParserTest {

    private GeminiResponseParser parser;

    @BeforeEach
    void setUp() {
        parser = new GeminiResponseParser(new ObjectMapper());
    }

    @Test
    @DisplayName("Parse valid raw JSON")
    void testParseValidJson() throws Exception {
        String json = "{\n" +
                "  \"category\": \"smartphone\",\n" +
                "  \"minPrice\": null,\n" +
                "  \"maxPrice\": 30000,\n" +
                "  \"preferredBrands\": [\"OnePlus\"],\n" +
                "  \"excludedBrands\": [],\n" +
                "  \"priorities\": [\"camera\", \"battery\"],\n" +
                "  \"requiredFeatures\": [\"5g\"],\n" +
                "  \"useCase\": null\n" +
                "}";

        InterpretedRequirements reqs = parser.parse(json);
        assertEquals("smartphone", reqs.getCategory());
        assertEquals(30000, reqs.getMaxPrice().intValue());
        assertEquals(1, reqs.getPreferredBrands().size());
        assertEquals("oneplus", reqs.getPreferredBrands().get(0));
        assertTrue(reqs.getPriorities().contains("camera"));
    }

    @Test
    @DisplayName("Parse JSON wrapped in markdown code fence")
    void testParseMarkdownCodeFence() throws Exception {
        String markdown = "```json\n" +
                "{\n" +
                "  \"category\": \"laptop\",\n" +
                "  \"maxPrice\": 70000,\n" +
                "  \"priorities\": [\"coding\"],\n" +
                "  \"useCase\": \"coding\"\n" +
                "}\n" +
                "```";

        InterpretedRequirements reqs = parser.parse(markdown);
        assertEquals("laptop", reqs.getCategory());
        assertEquals(70000, reqs.getMaxPrice().intValue());
        assertEquals("coding", reqs.getUseCase());
    }

    @Test
    @DisplayName("Malformed JSON should throw Exception for caller to trigger fallback")
    void testMalformedJsonThrowsException() {
        String malformed = "I couldn't find JSON here";
        assertThrows(Exception.class, () -> parser.parse(malformed));
    }
}
