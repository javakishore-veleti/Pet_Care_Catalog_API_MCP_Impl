package com.jk.labs.spring_ai.pet_care.catalog.common.util;

import com.jk.labs.spring_ai.pet_care.catalog.common.exception.ValidationException;

public class JsonUtil {

    public static String toJsonString(Object obj) {
        try {
            // Using a simple JSON representation for demonstration purposes.
            // In a real application, consider using a library like Jackson or Gson.
            return obj.toString();
        } catch (Exception e) {
            throw new ValidationException("Failed to convert object to JSON string: " + e.getMessage());
        }
    }
}