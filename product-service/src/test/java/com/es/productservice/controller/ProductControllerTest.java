package com.es.productservice.controller;

import com.es.productservice.dto.ProductRequestDTO;
import com.es.productservice.dto.ProductResponseDTO;
import com.es.productservice.exception.ProductNotFoundException;
import com.es.productservice.service.ProductService;
import com.es.productservice.util.ProductTestDataBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    // --- GET /products ---

    @Test
    void getProducts_returns200WithList() throws Exception {
        ProductResponseDTO responseDTO = ProductTestDataBuilder.buildResponseDTO();
        when(productService.getProducts()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(responseDTO.getId().toString()))
                .andExpect(jsonPath("$[0].name").value(responseDTO.getName()))
                .andExpect(jsonPath("$[0].price").value(29.99));
    }

    @Test
    void getProducts_whenEmpty_returns200WithEmptyList() throws Exception {
        when(productService.getProducts()).thenReturn(List.of());

        mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // --- GET /products/{id} ---

    @Test
    void getProductById_whenFound_returns200() throws Exception {
        ProductResponseDTO responseDTO = ProductTestDataBuilder.buildResponseDTO();
        when(productService.getProductById(ProductTestDataBuilder.DEFAULT_ID)).thenReturn(responseDTO);

        mockMvc.perform(get("/products/{id}", ProductTestDataBuilder.DEFAULT_ID).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDTO.getId().toString()))
                .andExpect(jsonPath("$.name").value(responseDTO.getName()));
    }

    @Test
    void getProductById_whenNotFound_returns404() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(productService.getProductById(randomId)).thenThrow(new ProductNotFoundException(randomId));

        mockMvc.perform(get("/products/{id}", randomId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.path").value("/products/" + randomId));
    }

    @Test
    void getProductById_whenMalformedUUID_returns400() throws Exception {
        mockMvc.perform(get("/products/not-a-uuid").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Type Mismatch"));
    }

    // --- POST /products ---

    @Test
    void createProduct_withValidRequest_returns201() throws Exception {
        ProductRequestDTO request = ProductTestDataBuilder.buildRequestDTO();
        ProductResponseDTO response = ProductTestDataBuilder.buildResponseDTO();
        when(productService.createProduct(any())).thenReturn(response);

        mockMvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.getId().toString()))
                .andExpect(jsonPath("$.name").value(response.getName()));
    }

    @Test
    void createProduct_withMissingFields_returns400WithFieldErrors() throws Exception {
        ProductRequestDTO invalidRequest = ProductTestDataBuilder.buildInvalidRequestDTO();

        mockMvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.fieldErrors").isArray())
                .andExpect(jsonPath("$.fieldErrors.length()").value(4));
    }

    // --- PUT /products/{id} ---

    @Test
    void updateProduct_whenFound_returns200() throws Exception {
        ProductRequestDTO request = ProductTestDataBuilder.buildRequestDTO();
        ProductResponseDTO response = ProductTestDataBuilder.buildResponseDTO();

        when(productService.updateProduct(eq(ProductTestDataBuilder.DEFAULT_ID), any())).thenReturn(response);

        mockMvc.perform(put("/products/{id}", ProductTestDataBuilder.DEFAULT_ID).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId().toString()));
    }

    @Test
    void updateProduct_whenNotFound_returns404() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(productService.updateProduct(eq(randomId), any())).thenThrow(new ProductNotFoundException(randomId));

        mockMvc.perform(put("/products/{id}", randomId).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ProductTestDataBuilder.buildRequestDTO())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    // --- DELETE /products/{id} ---

    @Test
    void deleteProduct_whenFound_returns204() throws Exception {
        mockMvc.perform(delete("/products/{id}", ProductTestDataBuilder.DEFAULT_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProduct_whenNotFound_returns404() throws Exception {
        UUID randomId = UUID.randomUUID();
        doThrow(new ProductNotFoundException(randomId)).when(productService)
                .deleteProduct(randomId);

        mockMvc.perform(delete("/products/{id}", randomId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

}
