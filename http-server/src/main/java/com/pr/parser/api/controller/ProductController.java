package com.pr.parser.api.controller;

import com.pr.parser.api.dto.PagedResponse;
import com.pr.parser.api.dto.ProductDto;
import com.pr.parser.api.dto.ProductRequest;
import com.pr.parser.api.mappers.ProductMapper;
import com.pr.parser.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
@Tag(name = "Product API", description = "API for managing products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @Operation(summary = "Get product by ID", description = "Retrieves a product by its unique ID")
    @GetMapping(value = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductDto getProduct(
            @Parameter(description = "Product ID", required = true) @PathVariable(name = "productId") Long productId) {
        return productService.getProduct(productId);
    }

    @Operation(summary = "Get all products with pagination", description = "Retrieves a paginated list of products")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<ProductDto>> getAllProducts(
            @Parameter(description = "Pagination offset") @RequestParam(value = "offset", defaultValue = "0") int offset,
            @Parameter(description = "Pagination limit") @RequestParam(value = "limit", defaultValue = "10") int limit
    ) {
        PagedResponse<ProductDto> response = productService.getAllProducts(offset, limit);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create a new product", description = "Saves a new product to the database")
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductDto saveProduct(
            @RequestPart("productRequest") ProductRequest productRequest
//            @RequestPart("file") MultipartFile file
    ) {
        return productService.saveProduct(productMapper.toModel(productRequest));
    }

    @Operation(summary = "Upload the file", description = "Saves the file")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> uploadFile(@RequestPart("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        productService.saveFile(file);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update product by ID", description = "Updates an existing product by its ID")
    @PutMapping(value = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductDto updateProduct(
            @Parameter(description = "Product ID to update", required = true) @PathVariable Long productId,
            @Parameter(description = "Updated product data") @RequestBody ProductRequest productRequest) {
        return productService.updateProduct(productId, productMapper.toModel(productRequest));
    }

    @Operation(summary = "Delete product by ID", description = "Deletes a product from the database by its ID")
    @DeleteMapping(value = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteProduct(
            @Parameter(description = "Product ID to delete", required = true) @PathVariable Long productId) {
        productService.deleteProduct(productId);
    }
}
