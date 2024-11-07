package com.pr.parser.api.mappers;

import com.pr.parser.api.dto.ProductDto;
import com.pr.parser.api.dto.ProductRequest;
import com.pr.parser.domain.entity.ProductEntity;
import com.pr.parser.domain.entity.ProductSpecificationEntity;
import com.pr.parser.domain.model.ProductModel;
import com.pr.parser.domain.model.ProductSpecificationModel;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

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

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "currency", source = "currencyModel")
    void updateEntityFromModel(@MappingTarget ProductEntity productEntity, ProductModel productModel);
}
