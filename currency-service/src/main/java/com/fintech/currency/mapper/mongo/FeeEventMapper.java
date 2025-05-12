package com.fintech.currency.mapper.mongo;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.fintech.currency.dto.mongo.FeeEventDTO;
import com.fintech.currency.model.mongo.FeeEvent;

@Mapper(componentModel = "spring")
public interface FeeEventMapper {
	FeeEventDTO toDto(FeeEvent entity);
    FeeEvent toEntity(FeeEventDTO dto);

    @BeanMapping(
        ignoreByDefault = true,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "transactionId", source = "transactionId")
    @Mapping(target = "feeAmount", source = "feeAmount")
    @Mapping(target = "currency", source = "currency")
    @Mapping(target = "timestamp", source = "timestamp")
    void updateFromDto(FeeEventDTO dto, @MappingTarget FeeEvent entity);
}
