package com.jk.labs.spring_ai.pet_care.catalog.common.constants;

public class AppConstants {

    private AppConstants() {
        throw new UnsupportedOperationException("This is a utility class");
    }

    // API Version
    public static final String API_VERSION = "v1";
    public static final String API_BASE_PATH = "/api/" + API_VERSION;

    // Package Constants
    public static final String PACKAGE_PATH = "/packages";
    public static final String SERVICE_PATH = "/services";
    public static final String RECOMMENDATION_PATH = "/recommendations";

    // MCP Constants
    public static final String MCP_BASE_PATH = "/mcp";
    public static final String MCP_TOOLS_PATH = MCP_BASE_PATH + "/tools";

    // Pagination
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;

    // Cache Names
    public static final String PACKAGE_CACHE = "packages";
    public static final String SERVICE_CACHE = "services";
}
