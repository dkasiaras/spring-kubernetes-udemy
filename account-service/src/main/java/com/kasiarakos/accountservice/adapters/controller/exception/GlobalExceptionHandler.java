package com.kasiarakos.accountservice.adapters.controller.exception;

import com.kasiarakos.accountservice.adapters.dto.ErrorResponseDto;
import com.kasiarakos.accountservice.domain.exception.CustomerAlreadyExistsException;
import com.kasiarakos.accountservice.domain.exception.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(CustomerAlreadyExistsException.class)
  public ResponseEntity<ErrorResponseDto> handle(
      CustomerAlreadyExistsException e, WebRequest webRequest) {
    var errorResponse =
        new ErrorResponseDto(
            webRequest.getDescription(false), BAD_REQUEST, e.getMessage(), LocalDateTime.now());
    return new ResponseEntity<>(errorResponse, BAD_REQUEST);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handle(
      ResourceNotFoundException e, WebRequest webRequest) {
    var errorResponse =
        new ErrorResponseDto(
            webRequest.getDescription(false), NOT_FOUND, e.getMessage(), LocalDateTime.now());
    return new ResponseEntity<>(errorResponse, NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDto> handle(Exception e, WebRequest webRequest) {
    var errorResponse =
        new ErrorResponseDto(
            webRequest.getDescription(false),
            INTERNAL_SERVER_ERROR,
            e.getMessage(),
            LocalDateTime.now());
    return new ResponseEntity<>(errorResponse, INTERNAL_SERVER_ERROR);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    Map<String, String> validationErrors = new HashMap<>();
    List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

    validationErrorList.forEach(
        error -> {
          String fieldName = ((FieldError) error).getField();
          String validationMessage = error.getDefaultMessage();
          validationErrors.put(fieldName, validationMessage);
        });

    return new ResponseEntity<>(validationErrors, BAD_REQUEST);
  }
}
