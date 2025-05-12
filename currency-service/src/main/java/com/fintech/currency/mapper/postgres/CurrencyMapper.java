package com.fintech.currency.mapper.postgres;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.fintech.currency.dto.postgres.CurrencyDTO;
import com.fintech.currency.model.postgres.Currency;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
	CurrencyDTO toDto(Currency entity);
    Currency toEntity(CurrencyDTO dto);

    @BeanMapping(
    	    ignoreByDefault = true, //точечно управлять маппингом
    	    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE //обновлять только непустые поля
    )
    @Mapping(target = "name", source = "name")
    @Mapping(target = "symbol", source = "symbol")
    void updateFromDto(CurrencyDTO dto, @MappingTarget Currency entity);
}
