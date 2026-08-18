package com.smartshop.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartshop.dto.response.ProductResponse;
import com.smartshop.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    private ProductMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductMapper(new ObjectMapper());
    }

    @Test
    @DisplayName("Convert Product entity to ProductResponse DTO with JSON attributes")
    void testEntityToDtoConversion() {
        Product p = new Product();
        p.setId(UUID.randomUUID());
        p.setName("MacBook Air M2");
        p.setBrand("Apple");
        p.setCategory("laptop");
        p.setPrice(BigDecimal.valueOf(89990));
        p.setSpecifications("{\"processor\": \"Apple M2\", \"ram\": \"8GB\", \"storage\": \"256GB SSD\"}");
        p.setFeatures("[\"Liquid Retina Display\", \"MagSafe 3 Charging\", \"Silent fanless design\"]");
        p.setPros("[\"Lightweight\", \"Exceptional battery life\"]");
        p.setCons("[\"8GB base RAM\", \"Single external display support\"]");
        p.setAvailability(true);

        ProductResponse res = mapper.toResponse(p);

        assertNotNull(res);
        assertEquals("MacBook Air M2", res.getName());
        assertEquals("Apple", res.getBrand());
        assertEquals("laptop", res.getCategory());
        assertTrue(res.getAvailability());

        assertTrue(res.getSpecifications() instanceof Map, "Specifications should parse to a Map");
        assertTrue(res.getFeatures() instanceof List, "Features should parse to a List");
        assertTrue(res.getPros() instanceof List, "Pros should parse to a List");
    }
}
