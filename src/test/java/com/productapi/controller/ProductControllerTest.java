package com.productapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.productapi.dto.ProductDTO;
import com.productapi.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDTO testProduct;

    @BeforeEach
    public void setup() {
        testProduct = new ProductDTO();
        testProduct.setId(1);
        testProduct.setName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(new BigDecimal("99.99"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetAllProducts() throws Exception {
        when(productService.getAllProducts())
                .thenReturn(CompletableFuture.completedFuture(Arrays.asList(testProduct)));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].name").value("Test Product"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testCreateProduct() throws Exception {
        when(productService.createProduct(any(ProductDTO.class)))
                .thenReturn(CompletableFuture.completedFuture(testProduct));

        ProductDTO newProduct = new ProductDTO();
        newProduct.setName("New Product");
        newProduct.setPrice(new BigDecimal("49.99"));

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isUnauthorized());
    }
}