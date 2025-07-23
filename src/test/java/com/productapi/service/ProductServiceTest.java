package com.productapi.service;

import com.productapi.dto.ProductDTO;
import com.productapi.entity.Product;
import com.productapi.repository.ProductRepository;
import com.productapi.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product testProduct;
    private ProductDTO testProductDTO;

    @BeforeEach
    public void setup() {
        testProduct = new Product();
        testProduct.setId(1);
        testProduct.setName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(new BigDecimal("99.99"));

        testProductDTO = new ProductDTO();
        testProductDTO.setId(1);
        testProductDTO.setName("Test Product");
        testProductDTO.setDescription("Test Description");
        testProductDTO.setPrice(new BigDecimal("99.99"));
    }

    @Test
    public void testGetAllProducts() throws Exception {
        when(productRepository.findAll()).thenReturn(Arrays.asList(testProduct));

        CompletableFuture<List<ProductDTO>> result = productService.getAllProducts();
        List<ProductDTO> products = result.get();

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Test Product", products.get(0).getName());
    }

    @Test
    public void testGetProductById() throws Exception {
        when(productRepository.findById(1)).thenReturn(Optional.of(testProduct));

        CompletableFuture<ProductDTO> result = productService.getProductById(1);
        ProductDTO product = result.get();

        assertNotNull(product);
        assertEquals("Test Product", product.getName());
    }

    @Test
    public void testCreateProduct() throws Exception {
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        CompletableFuture<ProductDTO> result = productService.createProduct(testProductDTO);
        ProductDTO product = result.get();

        assertNotNull(product);
        assertEquals("Test Product", product.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        when(productRepository.existsById(1)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1);

        CompletableFuture<Void> result = productService.deleteProduct(1);
        result.get();

        verify(productRepository, times(1)).deleteById(1);
    }
}