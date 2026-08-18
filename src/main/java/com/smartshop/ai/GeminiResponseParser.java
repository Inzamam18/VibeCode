package com.smartshop.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartshop.dto.common.InterpretedRequirements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class GeminiResponseParser {

    private static final Logger log = LoggerFactory.getLogger(GeminiResponseParser.class);
    private final ObjectMapper objectMapper;

    public GeminiResponseParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public InterpretedRequirements parse(String rawText) throws Exception {
        if (rawText == null || rawText.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty response from Gemini");
        }

        String cleanedJson = cleanJsonString(rawText);
        JsonNode root = objectMapper.readTree(cleanedJson);

        String category = root.hasNonNull("category") ? root.get("category").asText().toLowerCase().trim() : "smartphone";
        category = normalizeCategory(category);

        BigDecimal minPrice = null;
        if (root.hasNonNull("minPrice")) {
            double val = root.get("minPrice").asDouble();
            if (val >= 0) {
                minPrice = BigDecimal.valueOf(val);
            }
        }

        BigDecimal maxPrice = null;
        if (root.hasNonNull("maxPrice")) {
            double val = root.get("maxPrice").asDouble();
            if (val >= 0) {
                maxPrice = BigDecimal.valueOf(val);
            }
        }

        // Validate min <= max
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            BigDecimal temp = minPrice;
            minPrice = maxPrice;
            maxPrice = temp;
        }

        List<String> preferredBrands = extractStringList(root.get("preferredBrands"));
        List<String> excludedBrands = extractStringList(root.get("excludedBrands"));
        List<String> priorities = extractStringList(root.get("priorities"));
        List<String> requiredFeatures = extractStringList(root.get("requiredFeatures"));
        String useCase = root.hasNonNull("useCase") && !root.get("useCase").asText().equalsIgnoreCase("null")
                ? root.get("useCase").asText().trim() : null;

        return new InterpretedRequirements(
                category,
                minPrice,
                maxPrice,
                preferredBrands,
                excludedBrands,
                priorities,
                requiredFeatures,
                useCase
        );
    }

    private String cleanJsonString(String raw) {
        String trimmed = raw.trim();
        if (trimmed.startsWith("```json")) {
            trimmed = trimmed.substring(7);
        } else if (trimmed.startsWith("```")) {
            trimmed = trimmed.substring(3);
        }
        if (trimmed.endsWith("```")) {
            trimmed = trimmed.substring(0, trimmed.length() - 3);
        }
        return trimmed.trim();
    }

    private List<String> extractStringList(JsonNode node) {
        List<String> list = new ArrayList<>();
        if (node != null && node.isArray()) {
            for (JsonNode item : node) {
                if (item != null && !item.isNull() && !item.asText().isBlank()) {
                    list.add(item.asText().toLowerCase().trim());
                }
            }
        }
        return list;
    }

    private String normalizeCategory(String cat) {
        if (cat == null || cat.isBlank()) return "smartphone";
        cat = cat.toLowerCase();
        if (cat.contains("phone") || cat.contains("mobile")) return "smartphone";
        if (cat.contains("laptop") || cat.contains("macbook") || cat.contains("notebook")) return "laptop";
        if (cat.contains("headphone") || cat.contains("earphone") || cat.contains("earbud") || cat.contains("audio")) return "headphones";
        if (cat.contains("watch")) return "smartwatch";
        if (cat.contains("tablet") || cat.contains("ipad")) return "tablet";
        if (cat.contains("tv") || cat.contains("television")) return "television";
        return cat;
    }
}
