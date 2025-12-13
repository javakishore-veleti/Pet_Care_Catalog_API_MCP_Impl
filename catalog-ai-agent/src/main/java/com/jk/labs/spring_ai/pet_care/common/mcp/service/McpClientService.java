package com.jk.labs.spring_ai.pet_care.common.mcp.service;

import com.jk.labs.spring_ai.pet_care.common.mcp.model.ToolRequest;
import com.jk.labs.spring_ai.pet_care.common.mcp.model.ToolResponse;

public interface McpClientService {
    ToolResponse callTool(ToolRequest request);
    ToolResponse searchPackages(Object criteria);
    ToolResponse getPackageDetails(String packageCode);
    ToolResponse recommendPackage(Object profile);
    ToolResponse comparePackages(Object codes);
    ToolResponse calculateSavings(String packageCode);
    ToolResponse getServices(String category);
}
