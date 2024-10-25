package com.pr.parser.service;

import com.pr.parser.api.dto.PagedResponse;
import com.pr.parser.api.dto.ProductDto;
import com.pr.parser.entity.ProductEntity;
import com.pr.parser.mappers.ProductMapper;
import com.pr.parser.model.ProductModel;
import com.pr.parser.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public PagedResponse<ProductDto> getAllProducts(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<ProductEntity> productPage = productRepository.findAll(pageable);
        List<ProductDto> products = productMapper.toDto(productPage.getContent());

        return new PagedResponse<>(
                products,
                productPage.getNumber(),
                productPage.getTotalPages(),
                productPage.getTotalElements(),
                productPage.isLast()
        );
    }

    @Transactional
    public ProductDto saveProduct(ProductModel product) {
        appendCurrency(product, product.getCurrency());
        return productMapper.toDto(productRepository.save(productMapper.toEntity(product)));
    }

    @Transactional
    public ProductDto updateProduct(Long productId, ProductModel product) {
        ProductEntity existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        appendCurrency(product, product.getCurrency());
        productMapper.updateEntityFromModel(existingProduct, product);

        return productMapper.toDto(productRepository.save(existingProduct));
    }

    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Product not found: " + productId);
        }

        productRepository.deleteById(productId);
    }

    private void appendCurrency(ProductModel product, String currencyCode) {
        product.setCurrencyModel(currencyService.getByCode(currencyCode));
    }
}
