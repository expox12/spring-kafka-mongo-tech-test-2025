package com.avoris.hotel.validation;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(String timestamp, String message, String errorCode, List<FieldErrorResponse> errors) {

    public ErrorResponse(String message, String errorCode, List<FieldErrorResponse> errors) {
        this(LocalDateTime.now().toString(), message, errorCode, errors);
    }

    public static ErrorResponse of(String message, String errorCode, List<FieldErrorResponse> errors) {
        return new ErrorResponse(message, errorCode, errors);
    }
}
