package com.pr.parser.api.controller;

import com.pr.parser.api.dto.ProductDto;
import com.pr.parser.api.dto.ProductRequest;
import com.pr.parser.mappers.ProductMapper;
import com.pr.parser.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductDto saveProduct(@RequestBody ProductRequest productRequest) {
        return productService.saveProduct(productMapper.toModel(productRequest));
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateProduct() {
        // update product
    }
}