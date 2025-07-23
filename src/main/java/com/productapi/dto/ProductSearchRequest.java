package com.productapi.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductSearchRequest {
    private String name;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}