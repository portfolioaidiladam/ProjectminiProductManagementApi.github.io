package com.productapi.controller;

import com.productapi.dto.ApiResponse;
import com.productapi.dto.ProductDTO;
import com.productapi.dto.ProductSearchRequest;
import com.productapi.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public CompletableFuture<ResponseEntity<ApiResponse<List<ProductDTO>>>> getAllProducts() {
        log.info("Fetching all products");
        return productService.getAllProducts()
                .thenApply(products -> ResponseEntity.ok(ApiResponse.success(products)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public CompletableFuture<ResponseEntity<ApiResponse<ProductDTO>>> getProductById(@PathVariable int id) {
        log.info("Fetching product with id: {}", id);
        return productService.getProductById(id)
                .thenApply(product -> ResponseEntity.ok(ApiResponse.success(product)))
                .exceptionally(ex -> {
                    log.error("Error fetching product: {}", ex.getMessage());
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("Product not found"));
                });
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public CompletableFuture<ResponseEntity<ApiResponse<ProductDTO>>> createProduct(
            @Valid @RequestBody ProductDTO productDTO) {
        log.info("Creating new product: {}", productDTO.getName());
        return productService.createProduct(productDTO)
                .thenApply(product -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.success("Product created successfully", product)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public CompletableFuture<ResponseEntity<ApiResponse<ProductDTO>>> updateProduct(
            @PathVariable int id, @Valid @RequestBody ProductDTO productDTO) {
        log.info("Updating product with id: {}", id);
        return productService.updateProduct(id, productDTO)
                .thenApply(product -> ResponseEntity.ok(ApiResponse.success("Product updated successfully", product)))
                .exceptionally(ex -> {
                    log.error("Error updating product: {}", ex.getMessage());
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("Product not found"));
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public CompletableFuture<ResponseEntity<ApiResponse<String>>> deleteProduct(@PathVariable int id) {
        log.info("Deleting product with id: {}", id);
        return productService.deleteProduct(id)
                .thenApply(v -> ResponseEntity.ok(ApiResponse.<String>success("Product deleted successfully", null)))
                .exceptionally(ex -> {
                    log.error("Error deleting product: {}", ex.getMessage());
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.<String>error("Product not found"));
                });
    }

    @PostMapping("/search")
    @PreAuthorize("hasRole('USER')")
    public CompletableFuture<ResponseEntity<ApiResponse<List<ProductDTO>>>> searchProducts(
            @RequestBody ProductSearchRequest searchRequest) {
        log.info("Searching products with criteria: {}", searchRequest);
        return productService.searchProducts(searchRequest)
                .thenApply(products -> ResponseEntity.ok(ApiResponse.success(products)));
    }
}