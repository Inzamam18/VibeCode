package com.smartshop.service;

import com.smartshop.dto.response.ProductResponse;
import com.smartshop.entity.Product;
import com.smartshop.exception.ResourceNotFoundException;
import com.smartshop.mapper.ProductMapper;
import com.smartshop.repository.ProductRepository;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<ProductResponse> getProducts(String category, String brand, BigDecimal minPrice,
                                           BigDecimal maxPrice, BigDecimal minRating, String sort) {
        log.info("Fetching products with filters - category: {}, brand: {}, minPrice: {}, maxPrice: {}, minRating: {}, sort: {}",
                category, brand, minPrice, maxPrice, minRating, sort);

        Specification<Product> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (category != null && !category.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("category")), "%" + category.trim().toLowerCase() + "%"));
            }

            if (brand != null && !brand.trim().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("brand")), brand.trim().toLowerCase()));
            }

            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            if (minRating != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("rating"), minRating));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Sort sortOrder = parseSortOrder(sort);
        List<Product> products = productRepository.findAll(spec, sortOrder);

        return productMapper.toResponseList(products);
    }

    public ProductResponse getProductById(UUID id) {
        log.info("Fetching product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return productMapper.toResponse(product);
    }

    public List<ProductResponse> getProductsByCategory(String category) {
        log.info("Fetching products for category: {}", category);
        List<Product> products = productRepository.findByCategoryContainingIgnoreCase(category);
        return productMapper.toResponseList(products);
    }

    private Sort parseSortOrder(String sort) {
        if (sort == null || sort.trim().isEmpty()) {
            return Sort.unsorted();
        }

        String s = sort.trim().toLowerCase();
        return switch (s) {
            case "price_asc", "price,asc" -> Sort.by(Sort.Direction.ASC, "price");
            case "price_desc", "price,desc" -> Sort.by(Sort.Direction.DESC, "price");
            case "rating_desc", "rating,desc" -> Sort.by(Sort.Direction.DESC, "rating");
            case "discount_desc", "discount,desc" -> Sort.by(Sort.Direction.DESC, "discount");
            case "name_asc", "name,asc" -> Sort.by(Sort.Direction.ASC, "name");
            default -> Sort.unsorted();
        };
    }
}
