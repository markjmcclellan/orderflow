package com.orderflow.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDatabaseError(
            DataIntegrityViolationException ex) {

        Map<String, String> error = Map.of(
                "error", "Product could not be saved due to a database constraint."
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}