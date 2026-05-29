package com.es.productservice.mapper;

import com.es.productservice.dto.ProductRequestDTO;
import com.es.productservice.dto.ProductResponseDTO;
import com.es.productservice.model.Product;
import com.es.productservice.util.ProductTestDataBuilder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductMapperTest {

    // --- toDTO ---

    @Test
    void toDTO_mapsAllFields() {
        Product product = ProductTestDataBuilder.buildProduct();

        ProductResponseDTO dto = ProductMapper.toDTO(product);

        assertThat(dto.getId()).isEqualTo(product.getId());
        assertThat(dto.getName()).isEqualTo(product.getName());
        assertThat(dto.getDescription()).isEqualTo(product.getDescription());
        assertThat(dto.getPrice()).isEqualByComparingTo(product.getPrice());
        assertThat(dto.getCategory()).isEqualTo(product.getCategory());
        assertThat(dto.getQuantity()).isEqualTo(product.getQuantity());
    }

    @Test
    void toDTO_nullDescription_mapsToNull() {
        Product product = ProductTestDataBuilder.buildProduct();
        product.setDescription(null);

        ProductResponseDTO dto = ProductMapper.toDTO(product);

        assertThat(dto.getDescription()).isNull();
    }

    // --- toEntity ---

    @Test
    void toEntity_mapsAllFields() {
        ProductRequestDTO requestDTO = ProductTestDataBuilder.buildRequestDTO();

        Product product = ProductMapper.toEntity(requestDTO);

        assertThat(product.getName()).isEqualTo(requestDTO.getName());
        assertThat(product.getDescription()).isEqualTo(requestDTO.getDescription());
        assertThat(product.getPrice()).isEqualByComparingTo(requestDTO.getPrice());
        assertThat(product.getCategory()).isEqualTo(requestDTO.getCategory());
        assertThat(product.getQuantity()).isEqualTo(requestDTO.getQuantity());
    }

    @Test
    void toEntity_idIsNull() {
        ProductRequestDTO requestDTO = ProductTestDataBuilder.buildRequestDTO();

        Product product = ProductMapper.toEntity(requestDTO);

        assertThat(product.getId()).isNull();
    }

    // --- updateEntity ---

    @Test
    void updateEntity_updatesAllMutableFields() {
        Product existingProduct = ProductTestDataBuilder.buildProduct();
        UUID originalId = existingProduct.getId();

        ProductRequestDTO updateRequest = ProductTestDataBuilder.buildRequestDTO();
        updateRequest.setName("Updated Name");
        updateRequest.setPrice(new BigDecimal("49.99"));

        ProductMapper.updateEntity(existingProduct, updateRequest);

        assertThat(existingProduct.getId()).isEqualTo(originalId);
        assertThat(existingProduct.getName()).isEqualTo("Updated Name");
        assertThat(existingProduct.getPrice()).isEqualByComparingTo(new BigDecimal("49.99"));
        assertThat(existingProduct.getCategory()).isEqualTo(updateRequest.getCategory());
        assertThat(existingProduct.getQuantity()).isEqualTo(updateRequest.getQuantity());
    }
}
