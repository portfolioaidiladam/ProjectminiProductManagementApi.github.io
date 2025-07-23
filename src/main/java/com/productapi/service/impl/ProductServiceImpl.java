package com.productapi.service.impl;

import com.productapi.dto.ProductDTO;
import com.productapi.dto.ProductSearchRequest;
import com.productapi.entity.Product;
import com.productapi.repository.ProductRepository;
import com.productapi.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Async
    @Cacheable(value = "products")
    public CompletableFuture<List<ProductDTO>> getAllProducts() {
        log.info("Fetching all products");
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOs = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(productDTOs);
    }

    @Override
    @Async
    @Cacheable(value = "product", key = "#id")
    public CompletableFuture<ProductDTO> getProductById(int id) {
        log.info("Fetching product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return CompletableFuture.completedFuture(convertToDTO(product));
    }

    @Override
    @Async
    @CacheEvict(value = "products", allEntries = true)
    public CompletableFuture<ProductDTO> createProduct(ProductDTO productDTO) {
        log.info("Creating new product: {}", productDTO.getName());
        Product product = convertToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return CompletableFuture.completedFuture(convertToDTO(savedProduct));
    }

    @Override
    @Async
    @CachePut(value = "product", key = "#id")
    @CacheEvict(value = "products", allEntries = true)
    public CompletableFuture<ProductDTO> updateProduct(int id, ProductDTO productDTO) {
        log.info("Updating product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());

        Product updatedProduct = productRepository.save(product);
        return CompletableFuture.completedFuture(convertToDTO(updatedProduct));
    }

    @Override
    @Async
    @CacheEvict(value = {"product", "products"}, allEntries = true)
    public CompletableFuture<Void> deleteProduct(int id) {
        log.info("Deleting product with id: {}", id);
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<List<ProductDTO>> searchProducts(ProductSearchRequest searchRequest) {
        log.info("Searching products with criteria: {}", searchRequest);

        String name = searchRequest.getName() != null ? searchRequest.getName() : "";
        BigDecimal minPrice = searchRequest.getMinPrice() != null ? searchRequest.getMinPrice() : BigDecimal.ZERO;
        BigDecimal maxPrice = searchRequest.getMaxPrice() != null ? searchRequest.getMaxPrice() : new BigDecimal("999999999");

        List<Product> products = productRepository.searchProducts(name, minPrice, maxPrice);
        List<ProductDTO> productDTOs = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return CompletableFuture.completedFuture(productDTOs);
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setCreatedAt(product.getCreatedAt());
        return dto;
    }

    private Product convertToEntity(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        return product;
    }
}