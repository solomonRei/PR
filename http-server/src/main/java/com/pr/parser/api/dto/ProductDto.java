package com.pr.parser.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {
    private Long productId;
    private String name;
    private String description;
    private BigDecimal price;
    private String color;
    private String size;
    private String material;
    private CurrencyDto currency;
}
