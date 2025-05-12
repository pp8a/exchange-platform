package com.fintech.currency.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;
import java.util.UUID;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(UUID userId, BigDecimal current, BigDecimal attempted) {
        super("Insufficient funds for user " + userId + ": balance=" + current + ", tried to send=" + attempted);
    }
}
