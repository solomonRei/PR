package com.pr.parser.mappers;

import com.pr.parser.api.dto.ProductDto;
import com.pr.parser.api.dto.ProductRequest;
import com.pr.parser.entity.ProductEntity;
import com.pr.parser.entity.ProductSpecificationEntity;
import com.pr.parser.model.ProductModel;
import com.pr.parser.model.ProductSpecificationModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CurrencyMapper.class})
public interface ProductMapper {

    @Mapping(target = "productId", source = "id")
    @Mapping(target = "color", source = "productSpecification.color")
    @Mapping(target = "size", source = "productSpecification.size")
    @Mapping(target = "material", source = "productSpecification.material")
    ProductDto toDto(ProductEntity productEntity);

    List<ProductDto> toDto(List<ProductEntity> productEntities);

    ProductModel toModel(ProductRequest productRequest);
    ProductSpecificationModel toProductSpecificationModel(ProductRequest.ProductSpecificationRequest productSpecification);

    @Mapping(target = "currency", source = "currencyModel")
    ProductEntity toEntity(ProductModel product);
    ProductSpecificationEntity toProductSpecificationEntity(ProductModel productModel);
}
