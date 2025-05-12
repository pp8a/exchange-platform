package com.fintech.currency.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailAlreadyUsedException extends RuntimeException {   
	public EmailAlreadyUsedException(String email) {
        super("Email already in use: " + email);
    }
}
