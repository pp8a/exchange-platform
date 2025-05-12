package com.fintech.currency.service.api;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

import com.fintech.currency.dto.postgres.UserDTO;
import com.fintech.currency.dto.postgres.UserProjection;

public interface UserService {
    Mono<UserDTO> getById(UUID id);    
    Mono<UserDTO> create(UserDTO dto);
    Mono<UserDTO> update(UUID id, UserDTO dto);
    Flux<UserProjection> search(String name, String email, String phone, LocalDate dateOfBirth, int page, int size);
}
