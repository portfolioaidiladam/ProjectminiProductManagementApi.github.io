package com.productapi.service;

import com.productapi.dto.ProductDTO;
import com.productapi.dto.ProductSearchRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ProductService {
    CompletableFuture<List<ProductDTO>> getAllProducts();
    CompletableFuture<ProductDTO> getProductById(int id);
    CompletableFuture<ProductDTO> createProduct(ProductDTO productDTO);
    CompletableFuture<ProductDTO> updateProduct(int id, ProductDTO productDTO);
    CompletableFuture<Void> deleteProduct(int id);
    CompletableFuture<List<ProductDTO>> searchProducts(ProductSearchRequest searchRequest);
}