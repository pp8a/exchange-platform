package com.fintech.currency.mapper.mongo;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.fintech.currency.dto.mongo.CurrencyEventDTO;
import com.fintech.currency.model.mongo.CurrencyEvent;

@Mapper(componentModel = "spring")
public interface CurrencyEventMapper {
	CurrencyEvent toEntity(CurrencyEventDTO dto);
    CurrencyEventDTO toDto(CurrencyEvent entity);

    @BeanMapping(
        ignoreByDefault = true,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "oldRate", source = "oldRate")
    @Mapping(target = "newRate", source = "newRate")
    @Mapping(target = "currencyCode", source = "currencyCode")
    @Mapping(target = "timestamp", source = "timestamp")
    void updateFromDto(CurrencyEventDTO dto, @MappingTarget CurrencyEvent entity);
}


