package com.jk.labs.spring_ai.pet_care.catalog.common.util;

import com.jk.labs.spring_ai.pet_care.catalog.common.exception.ValidationException;

public final class ValidationUtil {

    private ValidationUtil() {
        throw new UnsupportedOperationException("This is a utility class");
    }

    public static void requireNonNull(Object obj, String message) {
        if (obj == null) {
            throw new ValidationException(message);
        }
    }

    public static void requireNonBlank(String str, String message) {
        if (str == null || str.trim().isEmpty()) {
            throw new ValidationException(message);
        }
    }
}