package com.fintech.currency.mapper.postgres;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.fintech.currency.dto.postgres.ExchangeRateDTO;
import com.fintech.currency.model.postgres.ExchangeRate;

@Mapper(componentModel = "spring")
public interface ExchangeRateMapper {
	ExchangeRateDTO toDto(ExchangeRate entity);
	ExchangeRate toEntity(ExchangeRateDTO dto);
	
	@BeanMapping(
		    ignoreByDefault = true,
		    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
		)
	@Mapping(target = "baseCurrency", source = "baseCurrency")
	@Mapping(target = "targetCurrency", source = "targetCurrency")
	@Mapping(target = "rate", source = "rate")
	@Mapping(target = "timestamp", source = "timestamp")
	void updateFromDto(ExchangeRateDTO dto, @MappingTarget ExchangeRate entity);


}
