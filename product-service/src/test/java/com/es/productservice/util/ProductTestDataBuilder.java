package com.es.productservice.util;

import com.es.productservice.dto.ProductRequestDTO;
import com.es.productservice.dto.ProductResponseDTO;
import com.es.productservice.model.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ProductTestDataBuilder {

    public static final UUID DEFAULT_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    public static final String DEFAULT_NAME = "Test Product";
    public static final String DEFAULT_DESCRIPTION = "Test Description";
    public static final BigDecimal DEFAULT_PRICE = new BigDecimal("29.99");
    public static final String DEFAULT_CATEGORY = "Electronics";
    public static final int DEFAULT_QUANTITY = 100;

    private ProductTestDataBuilder() {
    }

    public static Product buildProduct() {
        return Product.builder()
                .id(DEFAULT_ID)
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .price(DEFAULT_PRICE)
                .category(DEFAULT_CATEGORY)
                .quantity(DEFAULT_QUANTITY)
                .createdAt(LocalDateTime.of(2025, 1, 1, 12, 0))
                .updatedAt(LocalDateTime.of(2025, 1, 1, 12, 0))
                .build();
    }

    public static Product buildProductForSave(String name, String category, int quantity) {
        return Product.builder()
                .name(name)
                .description(DEFAULT_DESCRIPTION)
                .price(DEFAULT_PRICE)
                .category(category)
                .quantity(quantity)
                .build();
    }

    public static ProductRequestDTO buildRequestDTO() {
        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setName(DEFAULT_NAME);
        dto.setDescription(DEFAULT_DESCRIPTION);
        dto.setPrice(DEFAULT_PRICE);
        dto.setCategory(DEFAULT_CATEGORY);
        dto.setQuantity(DEFAULT_QUANTITY);
        return dto;
    }

    public static ProductRequestDTO buildInvalidRequestDTO() {
        return new ProductRequestDTO();
    }

    public static ProductResponseDTO buildResponseDTO() {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(DEFAULT_ID);
        dto.setName(DEFAULT_NAME);
        dto.setDescription(DEFAULT_DESCRIPTION);
        dto.setPrice(DEFAULT_PRICE);
        dto.setCategory(DEFAULT_CATEGORY);
        dto.setQuantity(DEFAULT_QUANTITY);
        return dto;
    }
}
