package com.avoris.hotel.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckInBeforeCheckOutValidator.class)
@Documented
public @interface CheckInBeforeCheckOut {
    String message() default "checkIn debe ser anterior a checkOut";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
