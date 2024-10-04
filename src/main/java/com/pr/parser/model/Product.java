package com.pr.parser.model;

import lombok.Data;

import java.util.Map;

@Data
public class Product {
    private String name;
    private String price;
    private String link;
    private Map<String, String> characteristics;
}
