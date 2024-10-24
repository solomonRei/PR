package com.pr.parser.service;

import com.pr.parser.api.dto.ProductDto;
import com.pr.parser.mappers.ProductMapper;
import com.pr.parser.entity.ProductEntity;
import com.pr.parser.model.ProductModel;
import com.pr.parser.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CurrencyService currencyService;
    private final ProductMapper productMapper;

    public ProductDto saveProduct(ProductModel product) {
        appendCurrency(product, product.getCurrency());
        ProductEntity productEntity = productMapper.toEntity(product);
        return productMapper.toDto(productRepository.save(productEntity));
    }

    private void appendCurrency(ProductModel product, String currencyCode) {
        product.setCurrencyModel(currencyService.getByCode(currencyCode));
    }
}