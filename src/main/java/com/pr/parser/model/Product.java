package com.pr.parser.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Product {
    private String name;
    private String price;
    private String link;
    private Map<String, String> characteristics;
}
