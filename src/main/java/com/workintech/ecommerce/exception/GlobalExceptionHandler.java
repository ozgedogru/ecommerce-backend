package com.workintech.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(CategoryException categoryException) {
        ErrorResponse errorResponse = new ErrorResponse(categoryException.getMessage());
        return new ResponseEntity<>(errorResponse, categoryException.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(ProductException productException) {
        ErrorResponse errorResponse = new ErrorResponse(productException.getMessage());
        return new ResponseEntity<>(errorResponse, productException.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
