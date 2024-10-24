package com.pr.parser.service;

import com.pr.parser.api.dto.ProductDto;
import com.pr.parser.entity.ProductEntity;
import com.pr.parser.mappers.ProductMapper;
import com.pr.parser.model.ProductModel;
import com.pr.parser.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CurrencyService currencyService;
    private final ProductMapper productMapper;

    public ProductDto getProduct(Long productId) {
        return productMapper.toDto(productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId)));
    }

    public List<ProductDto> getAllProducts() {
        return productMapper.toDto(productRepository.findAll());
    }

    public ProductDto saveProduct(ProductModel product) {
        appendCurrency(product, product.getCurrency());
        return productMapper.toDto(productRepository.save(productMapper.toEntity(product)));
    }

    public ProductDto updateProduct(Long productId, ProductModel product) {
        ProductEntity existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        appendCurrency(product, product.getCurrency());
        productMapper.updateEntityFromModel(existingProduct, product);

        return productMapper.toDto(productRepository.save(existingProduct));
    }

    private void appendCurrency(ProductModel product, String currencyCode) {
        product.setCurrencyModel(currencyService.getByCode(currencyCode));
    }
}
