package com.example.authservice.exceptions;

import javax.naming.AuthenticationException;

public class WrongPasswordException extends AuthenticationException {

    public WrongPasswordException(String message) {
        super(message);
    }

    public WrongPasswordException(String message, Throwable cause) {
        super(message);
    }
}
