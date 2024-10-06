package com.pr.parser.model;

import com.pr.parser.enums.Currency;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Product {
    private String name;
    private String price;
    private String link;
    private Currency currency;
    private Map<String, String> characteristics;
}
