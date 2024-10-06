package com.pr.parser.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
public class FilteredProductsResult {
    private List<Product> filteredProducts;
    private BigDecimal totalSum;
    private Instant timestamp;
}
