package com.avoris.hotel.validation;

import com.avoris.hotel.dto.ErrorResponse;
import com.avoris.hotel.dto.FieldErrorResponse;
import com.avoris.hotel.models.SearchNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<FieldErrorResponse> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldErrorResponse(
                        error.getField(),
                        error.getDefaultMessage()
                )).toList();
        ErrorResponse errorResponse = ErrorResponse.of("Validation failed", "VALIDATION_ERROR", errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SearchNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSearchNotFoundException(SearchNotFoundException ex) {
        ErrorResponse errorResponse = ErrorResponse.of(ex.getMessage(), "NOT_FOUND", Collections.emptyList());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}

