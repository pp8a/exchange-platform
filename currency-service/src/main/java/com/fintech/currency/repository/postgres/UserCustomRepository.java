package com.fintech.currency.repository.postgres;


import reactor.core.publisher.Flux;
import java.time.LocalDate;
import com.fintech.currency.dto.postgres.UserProjection;

public interface UserCustomRepository {
    Flux<UserProjection> findByFilters(String name, String email, String phone, 
    		LocalDate dateOfBirth, int page, int size);
}
