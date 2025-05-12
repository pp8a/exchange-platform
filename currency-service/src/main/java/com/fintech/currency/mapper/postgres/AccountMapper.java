package com.fintech.currency.mapper.postgres;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.fintech.currency.avro.TransferEvent;
import com.fintech.currency.dto.postgres.AccountDTO;
import com.fintech.currency.model.postgres.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDTO toDto(Account entity);
    Account toEntity(AccountDTO dto);

    @BeanMapping(
        ignoreByDefault = true,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "balance", source = "balance")
    @Mapping(target = "initialBalance", source = "initialBalance")
    void updateFromDto(AccountDTO dto, @MappingTarget Account entity);
    
    default TransferEvent toAvro(UUID from, UUID to, BigDecimal amount) {
        return TransferEvent.newBuilder()
                .setFromUserId(from.toString())
                .setToUserId(to.toString())
                .setAmount(amount.toPlainString())
                .setTimestamp(LocalDateTime.now().toString())
                .setDescription("Transfer from " + from + " to " + to + " of " + amount)
                .build();
    }
}
