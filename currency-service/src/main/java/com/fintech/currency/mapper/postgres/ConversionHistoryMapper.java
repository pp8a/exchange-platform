package com.fintech.currency.mapper.postgres;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.fintech.currency.dto.postgres.ConversionHistoryDTO;
import com.fintech.currency.model.postgres.ConversionHistory;

@Mapper(componentModel = "spring")
public interface ConversionHistoryMapper {
    ConversionHistoryDTO toDto(ConversionHistory entity);
    ConversionHistory toEntity(ConversionHistoryDTO dto);
    
    @BeanMapping(
    	    ignoreByDefault = true,
    	    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    	)
    @Mapping(target = "transactionId", source = "transactionId")
    @Mapping(target = "baseCurrency", source = "baseCurrency")
    @Mapping(target = "targetCurrency", source = "targetCurrency")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "exchangeRate", source = "exchangeRate")
    @Mapping(target = "spread", source = "spread")
    @Mapping(target = "commission", source = "commission")
    @Mapping(target = "timestamp", source = "timestamp")
    void updateFromDto(ConversionHistoryDTO dto, @MappingTarget ConversionHistory entity);
}
