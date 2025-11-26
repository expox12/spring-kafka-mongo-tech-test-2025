package com.avoris.hotel.dto;

import com.avoris.hotel.validation.CheckInBeforeCheckOut;
import jakarta.validation.constraints.*;

import java.util.List;

@CheckInBeforeCheckOut
public record SearchRequest(
        @NotNull(message = "No puede ser nulo")
        @NotBlank(message = "No debe quedar en blanco")
        String hotelId,

        @NotBlank(message = "No debe quedar en blanco")
        @NotNull(message = "No puede ser nulo")
        @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$", message = "La fecha debe ser dd/MM/yyyy")
        String checkIn,

        @NotBlank(message = "No debe quedar en blanco")
        @NotNull(message = "No puede ser nulo")
        @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$", message = "La fecha debe ser dd/MM/yyyy")
        String checkOut,

        @NotEmpty(message = "No puede estar vacío")
        @NotNull(message = "No puede ser nulo")
        List<@Min(0) Integer> ages
) {}
