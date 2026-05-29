package com.es.productservice.service;

import com.es.productservice.dto.ProductRequestDTO;
import com.es.productservice.dto.ProductResponseDTO;
import com.es.productservice.exception.ProductNotFoundException;
import com.es.productservice.model.Product;
import com.es.productservice.repository.ProductRepository;
import com.es.productservice.util.ProductTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    // --- getProducts ---

    @Test
    void getProducts_returnsAllProductsMappedAsDTOs() {
        Product product = ProductTestDataBuilder.buildProduct();
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductResponseDTO> result = productService.getProducts();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo(product.getId());
        assertThat(result.getFirst().getName()).isEqualTo(product.getName());
        assertThat(result.getFirst().getPrice()).isEqualByComparingTo(product.getPrice());
        assertThat(result.getFirst().getCategory()).isEqualTo(product.getCategory());
        assertThat(result.getFirst().getQuantity()).isEqualTo(product.getQuantity());
        verify(productRepository).findAll();
    }

    @Test
    void getProducts_whenNoProducts_returnsEmptyList() {
        when(productRepository.findAll()).thenReturn(List.of());

        List<ProductResponseDTO> result = productService.getProducts();

        assertThat(result).isEmpty();
        verify(productRepository).findAll();
    }

    // --- getProductById ---

    @Test
    void getProductById_whenFound_returnsMappedDTO() {
        Product product = ProductTestDataBuilder.buildProduct();
        when(productRepository.findById(ProductTestDataBuilder.DEFAULT_ID))
                .thenReturn(Optional.of(product));

        ProductResponseDTO result = productService.getProductById(ProductTestDataBuilder.DEFAULT_ID);

        assertThat(result.getId()).isEqualTo(product.getId());
        assertThat(result.getName()).isEqualTo(product.getName());
        assertThat(result.getPrice()).isEqualByComparingTo(product.getPrice());
        assertThat(result.getCategory()).isEqualTo(product.getCategory());
        assertThat(result.getQuantity()).isEqualTo(product.getQuantity());
    }

    @Test
    void getProductById_whenNotFound_throwsProductNotFoundException() {
        UUID randomId = UUID.randomUUID();
        when(productRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(randomId))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(randomId.toString());
    }

    // --- createProduct ---

    @Test
    void createProduct_savesAndReturnsMappedDTO() {
        ProductRequestDTO request = ProductTestDataBuilder.buildRequestDTO();
        Product savedProduct = ProductTestDataBuilder.buildProduct();

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductResponseDTO result = productService.createProduct(request);

        assertThat(result.getId()).isEqualTo(savedProduct.getId());
        assertThat(result.getName()).isEqualTo(savedProduct.getName());
        assertThat(result.getPrice()).isEqualByComparingTo(savedProduct.getPrice());

        verify(productRepository).save(any(Product.class));
    }

    // --- updateProduct ---

    @Test
    void updateProduct_whenFound_updatesAndReturnsMappedDTO() {
        Product existing = ProductTestDataBuilder.buildProduct();
        ProductRequestDTO request = ProductTestDataBuilder.buildRequestDTO();
        request.setName("Updated Name");
        request.setPrice(new BigDecimal("99.99"));

        when(productRepository.findById(ProductTestDataBuilder.DEFAULT_ID))
                .thenReturn(Optional.of(existing));
        when(productRepository.save(existing)).thenReturn(existing);

        ProductResponseDTO result = productService.updateProduct(
                ProductTestDataBuilder.DEFAULT_ID, request);

        assertThat(result.getName()).isEqualTo("Updated Name");
        assertThat(result.getPrice()).isEqualByComparingTo("99.99");
        verify(productRepository).save(existing);
    }

    @Test
    void updateProduct_whenNotFound_throwsProductNotFoundException() {
        UUID randomId = UUID.randomUUID();
        when(productRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.updateProduct(
                randomId, ProductTestDataBuilder.buildRequestDTO()))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(randomId.toString());
    }

    // --- deleteProduct ---

    @Test
    void deleteProduct_whenFound_deletesProduct() {
        Product product = ProductTestDataBuilder.buildProduct();
        when(productRepository.findById(ProductTestDataBuilder.DEFAULT_ID))
                .thenReturn(Optional.of(product));

        productService.deleteProduct(ProductTestDataBuilder.DEFAULT_ID);

        verify(productRepository).delete(product);
    }

    @Test
    void deleteProduct_whenNotFound_throwsProductNotFoundException() {
        UUID randomId = UUID.randomUUID();
        when(productRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.deleteProduct(randomId))
                .isInstanceOf(ProductNotFoundException.class);

        verify(productRepository, never()).delete(any());
    }
}