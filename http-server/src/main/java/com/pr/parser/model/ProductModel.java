package com.pr.parser.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductModel {
    private String name;
    private String description;
    private String currency;
    private BigDecimal price;
    private CurrencyModel currencyModel;
    private ProductSpecificationModel productSpecification;
}
