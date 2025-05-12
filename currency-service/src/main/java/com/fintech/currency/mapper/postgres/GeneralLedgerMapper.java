package com.fintech.currency.mapper.postgres;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.fintech.currency.dto.postgres.GeneralLedgerDTO;
import com.fintech.currency.model.postgres.GeneralLedger;

@Mapper(componentModel = "spring")
public interface GeneralLedgerMapper {
	GeneralLedgerDTO toDto(GeneralLedger entity);
	GeneralLedger toEntity(GeneralLedgerDTO dto);
	
	@BeanMapping(
		    ignoreByDefault = true,
		    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
		)
	@Mapping(target = "transactionId", source = "transactionId")
	@Mapping(target = "baseCurrency", source = "baseCurrency")
	@Mapping(target = "targetCurrency", source = "targetCurrency")
	@Mapping(target = "amount", source = "amount")
	@Mapping(target = "exchangeRate", source = "exchangeRate")
	@Mapping(target = "createdAt", source = "createdAt")
	void updateFromDto(GeneralLedgerDTO dto, @MappingTarget GeneralLedger entity);

}
