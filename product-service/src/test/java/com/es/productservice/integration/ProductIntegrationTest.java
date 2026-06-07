package com.es.productservice.integration;

import com.es.productservice.dto.ProductRequestDTO;
import com.es.productservice.dto.ProductResponseDTO;
import com.es.productservice.repository.ProductRepository;
import com.es.productservice.util.JwtTestHelper;
import com.es.productservice.util.ProductTestDataBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository productRepository;
    private String adminToken;
    private String userToken;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
        adminToken = JwtTestHelper.generateAdminToken();
        userToken = JwtTestHelper.generateUserToken();
    }

    // --- Full create product and read lifecycle ---

    @Test
    void createProduct_thenGetById_returnsPersistedProduct() throws Exception {
        ProductRequestDTO request = ProductTestDataBuilder.buildRequestDTO();

        MvcResult createResult = mockMvc.perform(post("/products").header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn();

        ProductResponseDTO created = objectMapper.readValue(createResult.getResponse()
                .getContentAsString(), ProductResponseDTO.class);

        mockMvc.perform(get("/products/{id}", created.getId()).header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.price").value(request.getPrice()))
                .andExpect(jsonPath("$.category").value(request.getCategory()));

        assertThat(productRepository.findById(created.getId())).isPresent();
    }

    // --- GET all products ---

    @Test
    void getAllProducts_returnsAllPersistedProducts() throws Exception {
        productRepository.save(ProductTestDataBuilder.buildProductForSave("Keyboard", "Electronics", 1));
        productRepository.save(ProductTestDataBuilder.buildProductForSave("Mouse", "Electronics", 2));

        mockMvc.perform(get("/products").header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // --- Update lifecycle ---

    @Test
    void updateProduct_persistsChanges() throws Exception {
        ProductResponseDTO created = objectMapper.readValue(mockMvc.perform(post("/products").header("Authorization",
                                "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ProductTestDataBuilder.buildRequestDTO())))
                .andReturn()
                .getResponse()
                .getContentAsString(), ProductResponseDTO.class);

        ProductRequestDTO update = ProductTestDataBuilder.buildRequestDTO();
        update.setName("Updated Keyboard");
        update.setQuantity(999);

        mockMvc.perform(put("/products/{id}", created.getId()).header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Keyboard"))
                .andExpect(jsonPath("$.quantity").value(999));

        assertThat(productRepository.findById(created.getId())).isPresent().get().satisfies(p -> {
            assertThat(p.getName()).isEqualTo("Updated Keyboard");
            assertThat(p.getQuantity()).isEqualTo(999);
        });
    }

    // --- Delete lifecycle ---

    @Test
    void deleteProduct_removesFromDatabase() throws Exception {
        ProductResponseDTO created = objectMapper.readValue(mockMvc.perform(post("/products").header("Authorization",
                                "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ProductTestDataBuilder.buildRequestDTO())))
                .andReturn()
                .getResponse()
                .getContentAsString(), ProductResponseDTO.class);

        mockMvc.perform(delete("/products/{id}", created.getId()).header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());

        assertThat(productRepository.findById(created.getId())).isEmpty();

        // second delete on same product id returns 404
        mockMvc.perform(delete("/products/{id}", created.getId()).header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    // --- Security tests ---

    @Test
    void getProducts_withNoToken_returns401() throws Exception {
        mockMvc.perform(get("/products")).andExpect(status().isUnauthorized());
    }

    @Test
    void createProduct_withUserRole_returns403() throws Exception {
        mockMvc.perform(post("/products").header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ProductTestDataBuilder.buildRequestDTO())))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateProduct_withUserRole_returns403() throws Exception {
        UUID id = productRepository.save(ProductTestDataBuilder.buildProductForSave("Keyboard", "Electronics", 1))
                .getId();

        mockMvc.perform(put("/products/{id}", id).header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ProductTestDataBuilder.buildRequestDTO())))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteProduct_withUserRole_returns403() throws Exception {
        UUID id = productRepository.save(ProductTestDataBuilder.buildProductForSave("Keyboard", "Electronics", 1))
                .getId();

        mockMvc.perform(delete("/products/{id}", id).header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void getProducts_withInvalidToken_returns401() throws Exception {
        mockMvc.perform(get("/products").header("Authorization", "Bearer this.is.not.a.valid.token"))
                .andExpect(status().isUnauthorized());
    }

    // --- Error paths ---

    @Test
    void getProductById_whenNotFound_returns404WithCorrectBody() throws Exception {
        UUID randomId = UUID.randomUUID();

        mockMvc.perform(get("/products/{id}", randomId).header("Authorization", "Bearer " + userToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.path").value("/products/" + randomId))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    void createProduct_withInvalidBody_returns400WithFieldErrors() throws Exception {
        mockMvc.perform(post("/products").header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ProductRequestDTO())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.fieldErrors").isArray())
                .andExpect(jsonPath("$.fieldErrors.length()").value(4));
    }
}
