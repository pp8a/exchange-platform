package com.fintech.currency.mapper.postgres;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.fintech.currency.dto.postgres.EmailDataDTO;
import com.fintech.currency.model.postgres.EmailData;

@Mapper(componentModel = "spring")
public interface EmailDataMapper {
    EmailDataDTO toDto(EmailData entity);
    EmailData toEntity(EmailDataDTO dto);

    @BeanMapping(
        ignoreByDefault = true,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "email", source = "email")
    @Mapping(target = "isVerified", source = "isVerified")
    void updateFromDto(EmailDataDTO dto, @MappingTarget EmailData entity);
}
