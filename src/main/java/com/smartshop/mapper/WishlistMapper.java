package com.smartshop.mapper;

import com.smartshop.dto.response.ProductResponse;
import com.smartshop.entity.WishlistItem;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class WishlistMapper {

    private final ProductMapper productMapper;

    public WishlistMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public List<ProductResponse> toProductResponseList(List<WishlistItem> items) {
        if (items == null) {
            return Collections.emptyList();
        }
        return items.stream()
                .map(WishlistItem::getProduct)
                .filter(Objects::nonNull)
                .map(productMapper::toResponse)
                .toList();
    }
}
