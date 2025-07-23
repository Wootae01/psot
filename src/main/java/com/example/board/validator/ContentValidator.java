package com.example.board.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Stack;

public class ContentValidator implements ConstraintValidator<NotEmptyContent, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        String withoutTag = value.replaceAll("<[^>]*>", "");
        //zero width space 제거
        String withoutZWSP = withoutTag.replaceAll("[\\p{Cf}\\p{Cc}]+", "");
        return !withoutZWSP.trim().isEmpty();
    }
}
