package com.jk.labs.spring_ai.pet_care.catalog.common.constants;

public class McpConstants {

    private McpConstants() {
        throw new UnsupportedOperationException("This is a utility class");
    }

    // Tool Names
    public static final String TOOL_SEARCH_PACKAGES = "search_packages";
    public static final String TOOL_GET_PACKAGE_DETAILS = "get_package_details";
    public static final String TOOL_RECOMMEND_PACKAGE = "recommend_package";
    public static final String TOOL_COMPARE_PACKAGES = "compare_packages";
    public static final String TOOL_CALCULATE_SAVINGS = "calculate_savings";
    public static final String TOOL_GET_SERVICES = "get_services";

    // MCP Protocol
    public static final String MCP_VERSION = "1.0.0";
    public static final String CONTENT_TYPE_MCP = "application/vnd.mcp+json";
}
