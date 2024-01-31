package com.kasiarakos.accountservice.domain.exception;

import java.text.MessageFormat;

public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
    super(
        MessageFormat.format(
            "{0} not found with the given input data {1}: {2}", resourceName, fieldName, fieldValue));
  }
}
