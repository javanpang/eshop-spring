package com.es.productservice.service;

import com.es.productservice.dto.ProductRequestDTO;
import com.es.productservice.dto.ProductResponseDTO;
import com.es.productservice.exception.ProductNotFoundException;
import com.es.productservice.mapper.ProductMapper;
import com.es.productservice.model.Product;
import com.es.productservice.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponseDTO> getProducts() {
        return productRepository.findAll().stream().map(ProductMapper::toDTO).toList();
    }

    public ProductResponseDTO getProductById(UUID id) {
        Product product = findProductOrThrow(id);
        return ProductMapper.toDTO(product);
    }

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO request) {
        Product product = ProductMapper.toEntity(request);
        Product saved = productRepository.save(product);
        return ProductMapper.toDTO(saved);
    }

    @Transactional
    public ProductResponseDTO updateProduct(UUID id, ProductRequestDTO request) {
        Product product = findProductOrThrow(id);
        ProductMapper.updateEntity(product, request);
        Product saved = productRepository.save(product);
        return ProductMapper.toDTO(saved);
    }

    @Transactional
    public void deleteProduct(UUID id) {
        Product product = findProductOrThrow(id);
        productRepository.delete(product);
    }

    private Product findProductOrThrow(UUID id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }
}
