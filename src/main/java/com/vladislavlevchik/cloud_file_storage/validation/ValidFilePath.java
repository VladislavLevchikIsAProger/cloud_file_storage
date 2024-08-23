package com.vladislavlevchik.cloud_file_storage.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FilePathValidator.class)
public @interface ValidFilePath {
    String message() default "Invalid file path";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
