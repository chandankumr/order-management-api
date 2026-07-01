package com.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 * Provides centralized exception handling and consistent error responses.
 * 
 * <p>This class intercepts exceptions thrown by controllers and transforms them
 * into appropriate HTTP responses with structured error messages.</p>
 * 
 * @author Chandan
 * @version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles OrderNotFoundException and returns a 404 Not Found response.
     *
     * @param ex the exception containing the error message
     * @return a ResponseEntity with 404 status and error details
     */
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(OrderNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles validation errors from @Valid annotations and returns a 400 Bad Request response.
     *
     * @param ex the exception containing field validation errors
     * @return a ResponseEntity with 400 status and field-specific error messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles DateTimeParseException when date parsing fails.
     *
     * @param ex the exception thrown during date parsing
     * @return a ResponseEntity with 400 status and a user-friendly message
     */
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Map<String, String>> handleDateParse(DateTimeParseException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid date format. Please use YYYY-MM.");
    }

    /**
     * Builds a standardized error response structure.
     *
     * @param status the HTTP status
     * @param message the error message
     * @return a ResponseEntity with the error structure
     */
    private ResponseEntity<Map<String, String>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", status.getReasonPhrase());
        error.put("message", message);
        return new ResponseEntity<>(error, status);
    }
}