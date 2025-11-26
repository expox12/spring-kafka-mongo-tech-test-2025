package com.avoris.hotel.validation;

import com.avoris.hotel.dto.SearchRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class CheckInBeforeCheckOutValidator implements ConstraintValidator<CheckInBeforeCheckOut, SearchRequest> {

    private static final DateTimeFormatter FORMATTER =
            new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("dd/MM/uuuu")
                    .toFormatter()
                    .withResolverStyle(ResolverStyle.STRICT);

    @Override
    public boolean isValid(SearchRequest request, ConstraintValidatorContext context) {
        if (request == null) return true;

        try {
            LocalDate checkInDate = LocalDate.parse(request.checkIn(), FORMATTER);
            LocalDate checkOutDate = LocalDate.parse(request.checkOut(), FORMATTER);

            if (!checkInDate.isBefore(checkOutDate)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        "checkIn (" + request.checkIn() + ") must be before checkOut (" + request.checkOut() + ")"
                ).addPropertyNode("checkIn").addConstraintViolation();
                return false;
            }

            return true;
        } catch (NullPointerException | DateTimeParseException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid date format")
                    .addConstraintViolation();
            return false;
        }
    }

}
