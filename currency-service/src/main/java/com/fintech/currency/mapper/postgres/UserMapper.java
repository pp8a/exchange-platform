package com.fintech.currency.mapper.postgres;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.fintech.currency.avro.UserEvent;
import com.fintech.currency.dto.postgres.UserDTO;
import com.fintech.currency.model.postgres.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	
    UserDTO toDto(User entity);
    
    @Mapping(target = "createdAt", ignore = true)
    User toEntity(UserDTO dto);

    @BeanMapping(
        ignoreByDefault = true,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "name", source = "name")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "password", source = "password")
    void updateFromDto(UserDTO dto, @MappingTarget User entity);
    
    default UserEvent toAvro(User user, String action) {
        return UserEvent.newBuilder()
                .setUserId(user.getId().toString())
                .setAction(action)
                .setTimestamp(user.getCreatedAt().toString()) // или LocalDateTime.now().toString()
                .setDetails("User name: " + user.getName())
                .build();
    }
    
}
