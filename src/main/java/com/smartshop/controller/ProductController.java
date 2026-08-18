package com.smartshop.controller;

import com.smartshop.dto.response.ProductResponse;
import com.smartshop.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Product catalog, filtering, and details APIs")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "Get all products with optional filters and sorting",
            description = "Supports filtering by category, brand, price range, minimum rating, and sorting (price_asc, price_desc, rating_desc, discount_desc).")
    public ResponseEntity<List<ProductResponse>> getProducts(
            @Parameter(description = "Category filter (e.g. smartphone, laptop, headphones)")
            @RequestParam(required = false) String category,

            @Parameter(description = "Brand filter (e.g. Apple, Samsung, Sony)")
            @RequestParam(required = false) String brand,

            @Parameter(description = "Minimum price filter")
            @RequestParam(required = false) BigDecimal minPrice,

            @Parameter(description = "Maximum price filter")
            @RequestParam(required = false) BigDecimal maxPrice,

            @Parameter(description = "Minimum rating filter (e.g. 4.0)")
            @RequestParam(required = false) BigDecimal minRating,

            @Parameter(description = "Sort order: price_asc, price_desc, rating_desc, discount_desc")
            @RequestParam(required = false) String sort
    ) {
        List<ProductResponse> products = productService.getProducts(category, brand, minPrice, maxPrice, minRating, sort);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get complete product details by ID",
            description = "Returns product details or HTTP 404 if the product does not exist.")
    public ResponseEntity<ProductResponse> getProductById(
            @Parameter(description = "Product UUID")
            @PathVariable UUID id
    ) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get products by category",
            description = "Returns all products matching the specified category.")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(
            @Parameter(description = "Category name (e.g. smartphone, laptop)")
            @PathVariable String category
    ) {
        List<ProductResponse> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }
}
