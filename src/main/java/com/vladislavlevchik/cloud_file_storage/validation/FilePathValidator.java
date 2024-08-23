package com.vladislavlevchik.cloud_file_storage.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FilePathValidator implements ConstraintValidator<ValidFilePath, String> {

    @Override
    public void initialize(ValidFilePath constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return value.isEmpty() || value.matches("([a-zA-Z0-9]+/)*[a-zA-Z0-9]+");
    }
}
