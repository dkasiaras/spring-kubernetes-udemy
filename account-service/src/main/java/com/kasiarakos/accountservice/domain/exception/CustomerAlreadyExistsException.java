package com.kasiarakos.accountservice.domain.exception;

public class CustomerAlreadyExistsException extends RuntimeException{

  public CustomerAlreadyExistsException(String message) {
    super(message);
  }
}
