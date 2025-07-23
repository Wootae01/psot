package com.example.board.validator;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ContentValidator.class)
public @interface NotEmptyContent {
    String message() default "내용이 비어있습니다.";

    Class[] groups() default {};

    Class[] payload() default {};
}
