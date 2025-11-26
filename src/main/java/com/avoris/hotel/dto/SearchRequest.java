package com.avoris.hotel.dto;

import com.avoris.hotel.validation.CheckInBeforeCheckOut;
import jakarta.validation.constraints.*;

import java.util.List;

@CheckInBeforeCheckOut
public record SearchRequest(
        @NotNull(message = "Cannot be null")
        @NotBlank(message = "Cannot be empty")
        String hotelId,

        @NotBlank(message = "Cannot be empty")
        @NotNull(message = "Cannot be null")
        @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$", message = "The date must be in the following format: dd/MM/yyyy")
        String checkIn,

        @NotBlank(message = "Cannot be empty")
        @NotNull(message = "Cannot be null")
        @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$", message = "The date must be in the following format: dd/MM/yyyy")
        String checkOut,

        @NotEmpty(message = "Cannot be empty")
        @NotNull(message = "Cannot be null")
        List<@Min(0) Integer> ages
) {}
