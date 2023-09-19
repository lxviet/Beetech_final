package com.beetech.springsecurity.web.validator;

import com.beetech.springsecurity.web.anotation.ValidDateFormat;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static com.beetech.springsecurity.domain.utility.Constants.Constants.VALID_DATE_FORMAT;

public class ValidDateFormatValidator implements ConstraintValidator<ValidDateFormat, String> {
    private static final String DATE_FORMAT = VALID_DATE_FORMAT;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;  // Null or empty values are handled by other annotations, such as @NotNull and @NotEmpty
        }

        try {
            LocalDate.parse(value, java.time.format.DateTimeFormatter.ofPattern(DATE_FORMAT));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
