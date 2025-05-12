package com.fintech.currency.mapper.mongo;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.fintech.currency.dto.mongo.ConversionEventDTO;
import com.fintech.currency.model.mongo.ConversionEvent;

@Mapper(componentModel = "spring")
public interface ConversionEventMapper {
	ConversionEventDTO toDto(ConversionEvent entity);
    ConversionEvent toEntity(ConversionEventDTO dto);

    @BeanMapping(
        ignoreByDefault = true,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "transactionId", source = "transactionId")
    @Mapping(target = "clientId", source = "clientId")
    @Mapping(target = "baseCurrency", source = "baseCurrency")
    @Mapping(target = "targetCurrency", source = "targetCurrency")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "exchangeRate", source = "exchangeRate")
    @Mapping(target = "timestamp", source = "timestamp")
    void updateFromDto(ConversionEventDTO dto, @MappingTarget ConversionEvent entity);
}
