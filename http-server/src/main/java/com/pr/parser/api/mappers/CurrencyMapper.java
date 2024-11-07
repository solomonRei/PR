package com.pr.parser.api.mappers;

import com.pr.parser.api.dto.CurrencyDto;
import com.pr.parser.domain.entity.CurrencyEntity;
import com.pr.parser.domain.model.CurrencyModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    @Mapping(target = "currencyId", source = "currencyId")
    CurrencyModel toModel(CurrencyEntity currency);

    List<CurrencyModel> toModel(List<CurrencyEntity> currency);

    CurrencyEntity toEntity(CurrencyModel currencyModel);

    CurrencyDto toDto(CurrencyEntity currencyEntity);

    List<CurrencyDto> toDto(List<CurrencyModel> currencyModel);

    CurrencyDto toDto(CurrencyModel currencyModel);
}
