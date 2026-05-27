package com.es.productservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ProductResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private Integer quantity;
}
