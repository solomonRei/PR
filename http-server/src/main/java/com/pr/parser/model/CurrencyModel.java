package com.pr.parser.model;

import lombok.Data;

@Data
public class CurrencyModel {
    private Long currencyId;
    private String name;
    private String code;
    private String symbol;
}
