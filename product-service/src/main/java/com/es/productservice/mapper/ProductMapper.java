package com.es.productservice.mapper;

import com.es.productservice.dto.ProductRequestDTO;
import com.es.productservice.dto.ProductResponseDTO;
import com.es.productservice.model.Product;

public class ProductMapper {

    private ProductMapper() {}

    public static ProductResponseDTO toDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();

        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setCategory(product.getCategory());
        dto.setQuantity(product.getQuantity());

        return dto;
    }

    public static Product toEntity(ProductRequestDTO dto) {
        Product product = new Product();

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setCategory(dto.getCategory());
        product.setQuantity(dto.getQuantity());

        return product;
    }

    public static void updateEntity(Product product, ProductRequestDTO dto) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setCategory(dto.getCategory());
        product.setQuantity(dto.getQuantity());
    }
}
