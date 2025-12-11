package com.jk.labs.spring_ai.pet_care.catalog.common.exception;

public class McpException extends RuntimeException {

    private final String toolName;

    public McpException(String message) {
        super(message);
        this.toolName = null;
    }

    public McpException(String message, String toolName) {
        super(message);
        this.toolName = toolName;
    }

    public McpException(String message, Throwable cause) {
        super(message, cause);
        this.toolName = null;
    }

    public String getToolName() {
        return toolName;
    }
}
