package com.smartshop.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartshop.dto.response.ProductResponse;
import com.smartshop.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class ProductMapper {

    private static final Logger log = LoggerFactory.getLogger(ProductMapper.class);
    private final ObjectMapper objectMapper;

    public ProductMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }

        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setBrand(product.getBrand());
        response.setCategory(product.getCategory());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setDiscount(product.getDiscount());
        response.setImageUrl(product.getImageUrl());
        response.setRating(product.getRating());
        response.setReviewCount(product.getReviewCount());
        response.setAvailability(product.getAvailability() != null ? product.getAvailability() : true);

        response.setSpecifications(parseJsonOrString(product.getSpecifications()));
        response.setFeatures(parseJsonOrList(product.getFeatures()));
        response.setPerformance(parseJsonOrString(product.getPerformance()));
        response.setPros(parseJsonOrList(product.getPros()));
        response.setCons(parseJsonOrList(product.getCons()));

        return response;
    }

    public List<ProductResponse> toResponseList(List<Product> products) {
        if (products == null) {
            return Collections.emptyList();
        }
        return products.stream()
                .map(this::toResponse)
                .toList();
    }

    private Object parseJsonOrString(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return null;
        }
        String trimmed = raw.trim();
        if ((trimmed.startsWith("{") && trimmed.endsWith("}")) || (trimmed.startsWith("[") && trimmed.endsWith("]"))) {
            try {
                return objectMapper.readValue(trimmed, Object.class);
            } catch (Exception e) {
                log.debug("Failed to parse JSON string '{}', returning as raw string", trimmed);
                return trimmed;
            }
        }
        return trimmed;
    }

    private Object parseJsonOrList(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String trimmed = raw.trim();
        if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
            try {
                return objectMapper.readValue(trimmed, new TypeReference<List<Object>>() {});
            } catch (Exception e) {
                log.debug("Failed to parse JSON array '{}', splitting by comma", trimmed);
            }
        }
        if (trimmed.contains(",")) {
            return List.of(trimmed.split("\\s*,\\s*"));
        }
        return List.of(trimmed);
    }
}
