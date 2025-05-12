package com.fintech.currency.mapper.postgres;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.fintech.currency.dto.postgres.PhoneDataDTO;
import com.fintech.currency.model.postgres.PhoneData;

@Mapper(componentModel = "spring")
public interface PhoneDataMapper {
    PhoneDataDTO toDto(PhoneData entity);
    PhoneData toEntity(PhoneDataDTO dto);

    @BeanMapping(
        ignoreByDefault = true,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "isVerified", source = "isVerified")
    void updateFromDto(PhoneDataDTO dto, @MappingTarget PhoneData entity);
}
