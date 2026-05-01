package com.example.authservice.exceptions;

public class UserAlreadyFoundException extends RuntimeException {

  public UserAlreadyFoundException(String message) {
    super(message);
  }

  public UserAlreadyFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
