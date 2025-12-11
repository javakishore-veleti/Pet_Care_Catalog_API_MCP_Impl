package com.jk.labs.spring_ai.pet_care.catalog.common.constants;

public class ErrorConstants {

    private ErrorConstants() {
        throw new UnsupportedOperationException("This is a utility class");
    }

    // Error Codes
    public static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String INTERNAL_ERROR = "INTERNAL_ERROR";
    public static final String MCP_ERROR = "MCP_ERROR";

    // Error Messages
    public static final String PACKAGE_NOT_FOUND = "Package not found with code: %s";
    public static final String SERVICE_NOT_FOUND = "Service not found with code: %s";
    public static final String INVALID_PET_TYPE = "Invalid pet type: %s";
    public static final String INVALID_AGE_GROUP = "Invalid age group: %s";
}
