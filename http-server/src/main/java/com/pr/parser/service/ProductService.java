package com.pr.parser.service;

import com.pr.parser.api.dto.PagedResponse;
import com.pr.parser.api.dto.ProductDto;
import com.pr.parser.domain.entity.ProductEntity;
import com.pr.parser.api.mappers.ProductMapper;
import com.pr.parser.domain.model.ProductModel;
import com.pr.parser.domain.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CurrencyService currencyService;
    private final FileService fileService;
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
//        try {
            appendCurrency(product, product.getCurrency());
//            fileService.saveFile(file);
            return productMapper.toDto(productRepository.save(productMapper.toEntity(product)));
//        } catch (IOException e) {
//            log.error("Exception during upload", e);
//            throw new RuntimeException("Failing to upload file");
//        }
    }

    @Transactional
    public void saveFile(MultipartFile file) {
        try {
            fileService.saveFile(file);
        } catch (IOException e) {
            log.error("Exception during upload", e);
            throw new RuntimeException("Failing to upload file");
        }
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
