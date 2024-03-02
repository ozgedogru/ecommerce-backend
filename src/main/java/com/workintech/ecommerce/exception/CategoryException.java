package com.workintech.ecommerce.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CategoryException extends RuntimeException {
    private HttpStatus status;

    public CategoryException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
