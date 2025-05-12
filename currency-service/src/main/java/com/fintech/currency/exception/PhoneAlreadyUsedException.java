package com.fintech.currency.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PhoneAlreadyUsedException extends RuntimeException {   
	public PhoneAlreadyUsedException(String phone) {
        super("Phone number already in use: " + phone);
    }
}