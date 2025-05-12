package com.fintech.currency.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

import org.springframework.http.HttpStatus;

import com.fintech.currency.dto.ErrorResponseDto;
import com.fintech.currency.exception.EmailAlreadyUsedException;
import com.fintech.currency.exception.InsufficientFundsException;
import com.fintech.currency.exception.PhoneAlreadyUsedException;
import com.fintech.currency.exception.UserNotFoundException;

import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ EmailAlreadyUsedException.class, PhoneAlreadyUsedException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponseDto> handleBadRequest(RuntimeException ex) {
        return Mono.just(new ErrorResponseDto(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        ));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ErrorResponseDto> handleNotFound(UserNotFoundException ex) {
        return Mono.just(new ErrorResponseDto(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        ));
    }
    
    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponseDto> handleInsufficientFunds(InsufficientFundsException ex) {
        return Mono.just(new ErrorResponseDto(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponseDto> handleIllegalArgument(IllegalArgumentException ex) {
        return Mono.just(new ErrorResponseDto(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        ));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ErrorResponseDto> handleOtherExceptions(Exception ex) {
        return Mono.just(new ErrorResponseDto(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Unexpected error: " + ex.getMessage()
        ));
    }
}
