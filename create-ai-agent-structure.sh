#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

BASE_DIR="catalog-ai-agent/src/main"
JAVA_BASE="$BASE_DIR/java/com/jk/labs/spring_ai/pet_care"
RESOURCES_BASE="$BASE_DIR/resources"

echo -e "${BLUE}Creating Pet Care AI Agent Module Structure...${NC}\n"

# Create base directories
mkdir -p "$JAVA_BASE"
mkdir -p "$RESOURCES_BASE/prompts"

# ==================== MAIN APPLICATION ====================
echo -e "${GREEN}Creating Main Application...${NC}"
cat > "$JAVA_BASE/CatalogAiAgentAppMain.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class CatalogAiAgentAppMain {

    public static void main(String[] args) {
        SpringApplication.run(CatalogAiAgentAppMain.class, args);

        System.out.println("""

            ╔════════════════════════════════════════════════════════════╗
            ║         Pet Care Catalog - AI Agent Started               ║
            ║                                                            ║
            ║  AI Agent running on: http://localhost:8083               ║
            ║  Health Check: http://localhost:8083/actuator/health      ║
            ║  Chat API: http://localhost:8083/api/chat                 ║
            ║                                                            ║
            ║  Multi-Agent System Active                                ║
            ║  Powered by Spring AI                                     ║
            ╚════════════════════════════════════════════════════════════╝
            """);
    }
}
EOF

# ==================== COMMON - MODELS ====================
echo -e "${GREEN}Creating Common - Models...${NC}"
mkdir -p "$JAVA_BASE/common/models/model"
mkdir -p "$JAVA_BASE/common/models/service"
mkdir -p "$JAVA_BASE/common/models/service/impl"
mkdir -p "$JAVA_BASE/common/models/config"

cat > "$JAVA_BASE/common/models/model/ChatMessage.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.models.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private String role;
    private String content;
    private Long timestamp;
}
EOF

cat > "$JAVA_BASE/common/models/model/LLMRequest.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.models.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LLMRequest {
    private String model;
    private List<ChatMessage> messages;
    private Double temperature;
    private Integer maxTokens;
    private Map<String, Object> tools;
}
EOF

cat > "$JAVA_BASE/common/models/model/LLMResponse.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.models.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LLMResponse {
    private String content;
    private String finishReason;
    private Integer tokensUsed;
}
EOF

cat > "$JAVA_BASE/common/models/service/LLMService.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.models.service;

import com.jk.labs.spring_ai.pet_care.common.models.model.LLMRequest;
import com.jk.labs.spring_ai.pet_care.common.models.model.LLMResponse;

public interface LLMService {
    LLMResponse chat(LLMRequest request);
    LLMResponse streamChat(LLMRequest request);
}
EOF

cat > "$JAVA_BASE/common/models/service/impl/OpenAiLLMServiceImpl.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.models.service.impl;

import com.jk.labs.spring_ai.pet_care.common.models.model.LLMRequest;
import com.jk.labs.spring_ai.pet_care.common.models.model.LLMResponse;
import com.jk.labs.spring_ai.pet_care.common.models.service.LLMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiLLMServiceImpl implements LLMService {

    @Override
    public LLMResponse chat(LLMRequest request) {
        // TODO: Implement OpenAI chat
        return null;
    }

    @Override
    public LLMResponse streamChat(LLMRequest request) {
        // TODO: Implement OpenAI streaming chat
        return null;
    }
}
EOF

cat > "$JAVA_BASE/common/models/config/OpenAiConfig.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.models.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.ai.openai")
public class OpenAiConfig {
    private String apiKey;
    private String model;
    private Double temperature;
    private Integer maxTokens;
}
EOF

# ==================== COMMON - CACHE ====================
echo -e "${GREEN}Creating Common - Cache...${NC}"
mkdir -p "$JAVA_BASE/common/cache/model"
mkdir -p "$JAVA_BASE/common/cache/service"
mkdir -p "$JAVA_BASE/common/cache/service/impl"
mkdir -p "$JAVA_BASE/common/cache/config"

cat > "$JAVA_BASE/common/cache/model/CacheEntry.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.cache.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheEntry {
    private String key;
    private Object value;
    private Long ttl;
    private Long timestamp;
}
EOF

cat > "$JAVA_BASE/common/cache/service/CacheService.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.cache.service;

public interface CacheService {
    void put(String key, Object value);
    void put(String key, Object value, long ttlSeconds);
    Object get(String key);
    void delete(String key);
    void clear();
}
EOF

cat > "$JAVA_BASE/common/cache/service/impl/InMemoryCacheServiceImpl.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.cache.service.impl;

import com.jk.labs.spring_ai.pet_care.common.cache.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InMemoryCacheServiceImpl implements CacheService {

    @Override
    public void put(String key, Object value) {
        // TODO: Implement in-memory cache
    }

    @Override
    public void put(String key, Object value, long ttlSeconds) {
        // TODO: Implement in-memory cache with TTL
    }

    @Override
    public Object get(String key) {
        // TODO: Implement cache retrieval
        return null;
    }

    @Override
    public void delete(String key) {
        // TODO: Implement cache deletion
    }

    @Override
    public void clear() {
        // TODO: Implement cache clear
    }
}
EOF

cat > "$JAVA_BASE/common/cache/config/CacheConfig.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.cache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "cache")
public class CacheConfig {
    private Boolean enabled = true;
    private Long defaultTtl = 3600L;
    private Integer maxSize = 1000;
}
EOF

# ==================== COMMON - VECTOR ====================
echo -e "${GREEN}Creating Common - Vector...${NC}"
mkdir -p "$JAVA_BASE/common/vector/model"
mkdir -p "$JAVA_BASE/common/vector/service"
mkdir -p "$JAVA_BASE/common/vector/service/impl"
mkdir -p "$JAVA_BASE/common/vector/config"

cat > "$JAVA_BASE/common/vector/model/Embedding.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.vector.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Embedding {
    private String id;
    private List<Float> vector;
    private String text;
    private Object metadata;
}
EOF

cat > "$JAVA_BASE/common/vector/model/VectorSearchResult.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.vector.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VectorSearchResult {
    private String id;
    private Float score;
    private String text;
    private Object metadata;
}
EOF

cat > "$JAVA_BASE/common/vector/service/VectorStoreService.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.vector.service;

import com.jk.labs.spring_ai.pet_care.common.vector.model.Embedding;
import com.jk.labs.spring_ai.pet_care.common.vector.model.VectorSearchResult;
import java.util.List;

public interface VectorStoreService {
    void store(Embedding embedding);
    List<VectorSearchResult> search(List<Float> queryVector, int topK);
    void delete(String id);
}
EOF

cat > "$JAVA_BASE/common/vector/service/impl/ChromaDbServiceImpl.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.vector.service.impl;

import com.jk.labs.spring_ai.pet_care.common.vector.model.Embedding;
import com.jk.labs.spring_ai.pet_care.common.vector.model.VectorSearchResult;
import com.jk.labs.spring_ai.pet_care.common.vector.service.VectorStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class ChromaDbServiceImpl implements VectorStoreService {

    @Override
    public void store(Embedding embedding) {
        // TODO: Implement ChromaDB storage
    }

    @Override
    public List<VectorSearchResult> search(List<Float> queryVector, int topK) {
        // TODO: Implement vector search
        return null;
    }

    @Override
    public void delete(String id) {
        // TODO: Implement deletion
    }
}
EOF

cat > "$JAVA_BASE/common/vector/config/VectorDbConfig.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.vector.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "vector.db")
public class VectorDbConfig {
    private Boolean enabled = false;
    private String provider = "chromadb";
    private String host = "localhost";
    private Integer port = 8000;
}
EOF

# ==================== COMMON - MEMORY ====================
echo -e "${GREEN}Creating Common - Memory...${NC}"
mkdir -p "$JAVA_BASE/common/memory/model"
mkdir -p "$JAVA_BASE/common/memory/service"
mkdir -p "$JAVA_BASE/common/memory/service/impl"
mkdir -p "$JAVA_BASE/common/memory/repository"

cat > "$JAVA_BASE/common/memory/model/ConversationHistory.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.memory.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationHistory {
    private String sessionId;
    private List<MemoryEntry> entries;
    private Integer maxEntries;
}
EOF

cat > "$JAVA_BASE/common/memory/model/MemoryEntry.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.memory.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemoryEntry {
    private String role;
    private String content;
    private Long timestamp;
    private Object metadata;
}
EOF

cat > "$JAVA_BASE/common/memory/service/MemoryService.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.memory.service;

import com.jk.labs.spring_ai.pet_care.common.memory.model.ConversationHistory;
import com.jk.labs.spring_ai.pet_care.common.memory.model.MemoryEntry;

public interface MemoryService {
    void addEntry(String sessionId, MemoryEntry entry);
    ConversationHistory getHistory(String sessionId);
    void clearHistory(String sessionId);
}
EOF

cat > "$JAVA_BASE/common/memory/service/impl/MemoryServiceImpl.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.memory.service.impl;

import com.jk.labs.spring_ai.pet_care.common.memory.model.ConversationHistory;
import com.jk.labs.spring_ai.pet_care.common.memory.model.MemoryEntry;
import com.jk.labs.spring_ai.pet_care.common.memory.service.MemoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MemoryServiceImpl implements MemoryService {

    @Override
    public void addEntry(String sessionId, MemoryEntry entry) {
        // TODO: Implement memory storage
    }

    @Override
    public ConversationHistory getHistory(String sessionId) {
        // TODO: Implement history retrieval
        return null;
    }

    @Override
    public void clearHistory(String sessionId) {
        // TODO: Implement history clearing
    }
}
EOF

cat > "$JAVA_BASE/common/memory/repository/MemoryRepository.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.memory.repository;

import com.jk.labs.spring_ai.pet_care.common.memory.model.ConversationHistory;
import org.springframework.stereotype.Repository;

@Repository
public class MemoryRepository {
    // TODO: Implement in-memory storage
}
EOF

# ==================== COMMON - MCP ====================
echo -e "${GREEN}Creating Common - MCP...${NC}"
mkdir -p "$JAVA_BASE/common/mcp/model"
mkdir -p "$JAVA_BASE/common/mcp/service"
mkdir -p "$JAVA_BASE/common/mcp/service/impl"
mkdir -p "$JAVA_BASE/common/mcp/config"

cat > "$JAVA_BASE/common/mcp/model/ToolRequest.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.mcp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolRequest {
    private String toolName;
    private Map<String, Object> arguments;
}
EOF

cat > "$JAVA_BASE/common/mcp/model/ToolResponse.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.mcp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolResponse {
    private Boolean success;
    private Object content;
    private String error;
}
EOF

cat > "$JAVA_BASE/common/mcp/service/McpClientService.java" << 'EOF'
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
EOF

cat > "$JAVA_BASE/common/mcp/service/impl/McpRestClientServiceImpl.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.mcp.service.impl;

import com.jk.labs.spring_ai.pet_care.common.mcp.model.ToolRequest;
import com.jk.labs.spring_ai.pet_care.common.mcp.model.ToolResponse;
import com.jk.labs.spring_ai.pet_care.common.mcp.service.McpClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class McpRestClientServiceImpl implements McpClientService {

    @Override
    public ToolResponse callTool(ToolRequest request) {
        // TODO: Implement MCP tool call
        return null;
    }

    @Override
    public ToolResponse searchPackages(Object criteria) {
        // TODO: Implement search_packages call
        return null;
    }

    @Override
    public ToolResponse getPackageDetails(String packageCode) {
        // TODO: Implement get_package_details call
        return null;
    }

    @Override
    public ToolResponse recommendPackage(Object profile) {
        // TODO: Implement recommend_package call
        return null;
    }

    @Override
    public ToolResponse comparePackages(Object codes) {
        // TODO: Implement compare_packages call
        return null;
    }

    @Override
    public ToolResponse calculateSavings(String packageCode) {
        // TODO: Implement calculate_savings call
        return null;
    }

    @Override
    public ToolResponse getServices(String category) {
        // TODO: Implement get_services call
        return null;
    }
}
EOF

cat > "$JAVA_BASE/common/mcp/config/McpClientConfig.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.mcp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mcp.client")
public class McpClientConfig {
    private String baseUrl = "http://localhost:8082";
    private Integer timeout = 30000;
    private Integer retries = 3;
}
EOF

# ==================== COMMON - EXCEPTION ====================
echo -e "${GREEN}Creating Common - Exceptions...${NC}"
mkdir -p "$JAVA_BASE/common/exception"

cat > "$JAVA_BASE/common/exception/AgentException.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.exception;

public class AgentException extends RuntimeException {
    public AgentException(String message) {
        super(message);
    }

    public AgentException(String message, Throwable cause) {
        super(message, cause);
    }
}
EOF

cat > "$JAVA_BASE/common/exception/LLMException.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.exception;

public class LLMException extends RuntimeException {
    public LLMException(String message) {
        super(message);
    }

    public LLMException(String message, Throwable cause) {
        super(message, cause);
    }
}
EOF

cat > "$JAVA_BASE/common/exception/ToolExecutionException.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.exception;

public class ToolExecutionException extends RuntimeException {
    public ToolExecutionException(String message) {
        super(message);
    }

    public ToolExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
EOF

# ==================== COMMON - UTIL ====================
echo -e "${GREEN}Creating Common - Utilities...${NC}"
mkdir -p "$JAVA_BASE/common/util"

cat > "$JAVA_BASE/common/util/PromptBuilder.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PromptBuilder {

    public static String buildSystemPrompt(String agentType) {
        // TODO: Implement prompt building
        return null;
    }

    public static String buildUserPrompt(String input, Object context) {
        // TODO: Implement user prompt building
        return null;
    }
}
EOF

cat > "$JAVA_BASE/common/util/JsonUtils.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonUtils {

    public static String toJson(Object object) {
        // TODO: Implement JSON serialization
        return null;
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        // TODO: Implement JSON deserialization
        return null;
    }
}
EOF

# ==================== COMMON - CONSTANT ====================
echo -e "${GREEN}Creating Common - Constants...${NC}"
mkdir -p "$JAVA_BASE/common/constant"

cat > "$JAVA_BASE/common/constant/AppConstants.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.common.constant;

public final class AppConstants {

    private AppConstants() {}

    // Agent Types
    public static final String ADVISOR_AGENT = "advisor";
    public static final String RESEARCH_AGENT = "research";
    public static final String RECOMMENDATION_AGENT = "recommendation";
    public static final String COMPARISON_AGENT = "comparison";
    public static final String SALES_AGENT = "sales";

    // MCP Tools
    public static final String TOOL_SEARCH_PACKAGES = "search_packages";
    public static final String TOOL_GET_PACKAGE_DETAILS = "get_package_details";
    public static final String TOOL_RECOMMEND_PACKAGE = "recommend_package";
    public static final String TOOL_COMPARE_PACKAGES = "compare_packages";
    public static final String TOOL_CALCULATE_SAVINGS = "calculate_savings";
    public static final String TOOL_GET_SERVICES = "get_services";
}
EOF

# ==================== BUSINESS FEATURES ====================

# Function to create business feature structure
create_feature() {
    local feature=$1
    local feature_cap=$(echo ${feature:0:1} | tr '[:lower:]' '[:upper:]')${feature:1}

    echo -e "${GREEN}Creating Feature: $feature...${NC}"

    mkdir -p "$JAVA_BASE/$feature/api"
    mkdir -p "$JAVA_BASE/$feature/service"
    mkdir -p "$JAVA_BASE/$feature/service/impl"
    mkdir -p "$JAVA_BASE/$feature/model"
    mkdir -p "$JAVA_BASE/$feature/controller"
    mkdir -p "$JAVA_BASE/$feature/config"

    # API Interface
    cat > "$JAVA_BASE/$feature/api/${feature_cap}Agent.java" << EOF
package com.jk.labs.spring_ai.pet_care.$feature.api;

public interface ${feature_cap}Agent {
    // TODO: Define agent interface methods
}
EOF

    # Service Interface
    cat > "$JAVA_BASE/$feature/service/${feature_cap}Service.java" << EOF
package com.jk.labs.spring_ai.pet_care.$feature.service;

public interface ${feature_cap}Service {
    // TODO: Define service interface methods
}
EOF

    # Service Implementation
    cat > "$JAVA_BASE/$feature/service/impl/${feature_cap}ServiceImpl.java" << EOF
package com.jk.labs.spring_ai.pet_care.$feature.service.impl;

import com.jk.labs.spring_ai.pet_care.$feature.service.${feature_cap}Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ${feature_cap}ServiceImpl implements ${feature_cap}Service {
    // TODO: Implement service methods
}
EOF

    # Request Model
    cat > "$JAVA_BASE/$feature/model/${feature_cap}Request.java" << EOF
package com.jk.labs.spring_ai.pet_care.$feature.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ${feature_cap}Request {
    // TODO: Define request fields
}
EOF

    # Response Model
    cat > "$JAVA_BASE/$feature/model/${feature_cap}Response.java" << EOF
package com.jk.labs.spring_ai.pet_care.$feature.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ${feature_cap}Response {
    // TODO: Define response fields
}
EOF

    # Controller
    cat > "$JAVA_BASE/$feature/controller/${feature_cap}Controller.java" << EOF
package com.jk.labs.spring_ai.pet_care.$feature.controller;

import com.jk.labs.spring_ai.pet_care.$feature.service.${feature_cap}Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/$feature")
@RequiredArgsConstructor
public class ${feature_cap}Controller {

    private final ${feature_cap}Service ${feature}Service;

    // TODO: Implement controller endpoints
}
EOF

    # Config
    cat > "$JAVA_BASE/$feature/config/${feature_cap}Config.java" << EOF
package com.jk.labs.spring_ai.pet_care.$feature.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "agent.$feature")
public class ${feature_cap}Config {
    private Boolean enabled = true;
    private String model = "gpt-4o-mini";
    private Double temperature = 0.7;
}
EOF
}

# Create all business features
create_feature "advisor"
create_feature "research"
create_feature "recommendation"
create_feature "comparison"
create_feature "sales"

# ==================== ORCHESTRATION FEATURE ====================
echo -e "${GREEN}Creating Feature: orchestration...${NC}"
mkdir -p "$JAVA_BASE/orchestration/api"
mkdir -p "$JAVA_BASE/orchestration/service"
mkdir -p "$JAVA_BASE/orchestration/service/impl"
mkdir -p "$JAVA_BASE/orchestration/model"
mkdir -p "$JAVA_BASE/orchestration/strategy"
mkdir -p "$JAVA_BASE/orchestration/controller"
mkdir -p "$JAVA_BASE/orchestration/config"

cat > "$JAVA_BASE/orchestration/api/Orchestrator.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.orchestration.api;

public interface Orchestrator {
    // TODO: Define orchestrator interface
}
EOF

cat > "$JAVA_BASE/orchestration/service/OrchestrationService.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.orchestration.service;

public interface OrchestrationService {
    // TODO: Define orchestration service methods
}
EOF

cat > "$JAVA_BASE/orchestration/service/impl/OrchestrationServiceImpl.java" << 'EOF'
package com.jk.labs.spring_ai.pet_care.orchestration.service.impl;

import com.jk.labs.spring_ai.pet_care.orchestration.service.OrchestrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrchestrationServiceImpl implements OrchestrationService {
    // TODO: Implement orchestration logic
}
EOF

cat > "$JAVA_BASE/orchestration/model/OrchestrationRequest.java"
                         package com.jk.labs.spring_ai.pet_care.orchestration.model;
                         import lombok.AllArgsConstructor;
                         import lombok.Builder;
                         import lombok.Data;
                         import lombok.NoArgsConstructor;
                         @Data
                         @Builder
                         @NoArgsConstructor
                         @AllArgsConstructor
                         public class OrchestrationRequest {
                         private String query;
                         private String sessionId;
                         }
                         EOF
                         cat > "$JAVA_BASE/orchestration/model/OrchestrationResponse.java" << 'EOF'
                         package com.jk.labs.spring_ai.pet_care.orchestration.model;
                         import lombok.AllArgsConstructor;
                         import lombok.Builder;
                         import lombok.Data;
                         import lombok.NoArgsConstructor;
                         @Data
                         @Builder
                         @NoArgsConstructor
                         @AllArgsConstructor
                         public class OrchestrationResponse {
                         private String response;
                         private String agentUsed;
                         }
                         EOF
                         cat > "$JAVA_BASE/orchestration/model/AgentTask.java" << 'EOF'
                         package com.jk.labs.spring_ai.pet_care.orchestration.model;
                         import lombok.AllArgsConstructor;
                         import lombok.Builder;
                         import lombok.Data;
                         import lombok.NoArgsConstructor;
                         @Data
                         @Builder
                         @NoArgsConstructor
                         @AllArgsConstructor
                         public class AgentTask {
                         private String agentType;
                         private Object input;
                         private Integer priority;
                         }
                         EOF
                         cat > "$JAVA_BASE/orchestration/model/ExecutionPlan.java" << 'EOF'
                         package com.jk.labs.spring_ai.pet_care.orchestration.model;
                         import lombok.AllArgsConstructor;
                         import lombok.Builder;
                         import lombok.Data;
                         import lombok.NoArgsConstructor;
                         import java.util.List;
                         @Data
                         @Builder
                         @NoArgsConstructor
                         @AllArgsConstructor
                         public class ExecutionPlan {
                         private List<AgentTask> tasks;
                         private String strategy;
                         }
                         EOF
                         cat > "$JAVA_BASE/orchestration/strategy/RoutingStrategy.java" << 'EOF'
                         package com.jk.labs.spring_ai.pet_care.orchestration.strategy;
                         public interface RoutingStrategy {
                         String determineAgent(String query);
                         }
                         EOF
                         cat > "$JAVA_BASE/orchestration/strategy/AutoRoutingStrategy.java" << 'EOF'
                         package com.jk.labs.spring_ai.pet_care.orchestration.strategy;
                         import lombok.extern.slf4j.Slf4j;
                         import org.springframework.stereotype.Component;
                         @Slf4j
                         @Component
                         public class AutoRoutingStrategy implements RoutingStrategy {
                         @Override
                         public String determineAgent(String query) {
                             // TODO: Implement auto routing logic
                             return null;
                         }
                         }
                         EOF
                         cat > "$JAVA_BASE/orchestration/strategy/SequentialRoutingStrategy.java" << 'EOF'
                         package com.jk.labs.spring_ai.pet_care.orchestration.strategy;
                         import lombok.extern.slf4j.Slf4j;
                         import org.springframework.stereotype.Component;
                         @Slf4j
                         @Component
                         public class SequentialRoutingStrategy implements RoutingStrategy {
                         @Override
                         public String determineAgent(String query) {
                             // TODO: Implement sequential routing logic
                             return null;
                         }
                         }
                         EOF
                         cat > "$JAVA_BASE/orchestration/controller/OrchestrationController.java" << 'EOF'
                         package com.jk.labs.spring_ai.pet_care.orchestration.controller;
                         import com.jk.labs.spring_ai.pet_care.orchestration.service.OrchestrationService;
                         import lombok.RequiredArgsConstructor;
                         import lombok.extern.slf4j.Slf4j;
                         import org.springframework.web.bind.annotation.*;
                         @Slf4j
                         @RestController
                         @RequestMapping("/api/orchestration")
                         @RequiredArgsConstructor
                         public class OrchestrationController {
                         private final OrchestrationService orchestrationService;

                         // TODO: Implement controller endpoints
                         }
                         EOF
                         cat > "$JAVA_BASE/orchestration/config/OrchestrationConfig.java" << 'EOF'
                         package com.jk.labs.spring_ai.pet_care.orchestration.config;
                         import lombok.Data;
                         import org.springframework.boot.context.properties.ConfigurationProperties;
                         import org.springframework.context.annotation.Configuration;
                         @Data
                         @Configuration
                         @ConfigurationProperties(prefix = "orchestration")
                         public class OrchestrationConfig {
                         private String strategy = "AUTO";
                         private Integer maxAgentCalls = 5;
                         private Boolean enableHandoff = true;
                         }
                         EOF
                         ==================== CONVERSATION FEATURE ====================
                         echo -e "GREENCreatingFeature:conversation...{GREEN}Creating Feature: conversation...
                         GREENCreatingFeature:conversation...{NC}"
                         mkdir -p "$JAVA_BASE/conversation/api"
                         mkdir -p "$JAVA_BASE/conversation/service"
                         mkdir -p "$JAVA_BASE/conversation/service/impl"
                         mkdir -p "$JAVA_BASE/conversation/model"
                         mkdir -p "$JAVA_BASE/conversation/controller"
                         mkdir -p "$JAVA_BASE/conversation/config"

                         cat > "$JAVA_BASE/conversation/api/ConversationManager.java" << 'EOF'
                         package com.jk.labs.spring_ai.pet_care.conversation.api;
                         public interface ConversationManager {
                         // TODO: Define conversation manager interface
                         }
                         EOF
                         cat > "$JAVA_BASE/conversation/service/ConversationService.java" << 'EOF'
                         package com.jk.labs.spring_ai.pet_care.conversation.service;
                         public interface ConversationService {
                         // TODO: Define conversation service methods
                         }
                         EOF
                         cat > "$JAVA_BASE/conversation/service/impl/ConversationServiceImpl.java" << 'EOF'
                         package com.jk.labs.spring_ai.pet_care.conversation.service.impl;
                         import com.jk.labs.spring_ai.pet_care.conversation.service.ConversationService;
                         import lombok.RequiredArgsConstructor;
                         import lombok.extern.slf4j.Slf4j;
                         import org.springframework.stereotype.Service;
                         @Slf4j
                         @Service
                         @RequiredArgsConstructor
                         public class ConversationServiceImpl implements ConversationService {
                         // TODO: Implement conversation service
                         }
                         EOF
                         cat > "$JAVA_BASE/conversation/model/ChatRequest.java" << 'EOF'
                         package com.jk.labs.spring_ai.pet_care.conversation.model;
                         import lombok.AllArgsConstructor;
                         import lombok.Builder;
                         import lombok.Data;
                         import lombok.NoArgsConstructor;
                         @Data
                         @Builder
                         @NoArgsConstructor
                         @AllArgsConstructor
                         public class ChatRequest {
                         private String message;
                         private String sessionId;
                         private String userId;
                         }
                         EOF
                         cat > "$JAVA_BASE/conversation/model/ChatResponse.java" << 'EOF'
                         package com.jk.labs.spring_ai.pet_care.conversation.model;
                         import lombok.AllArgsConstructor;
                         import lombok.Builder;
                         import lombok.Data;
                         import lombok.NoArgsConstructor;
                         @Data
                         @Builder
                         @NoArgsConstructor
                         @AllArgsConstructor
                         public class ChatResponse {
                         private String response;
                         private String sessionId;
                         private String agentUsed;
                         }
                         EOF
                         cat > "$JAVA_BASE/conversation/model/ConversationSession.java" << 'EOF'
                         package com.jk.labs.spring_ai.pet_care.conversation.model;
                         import lombok.AllArgsConstructor;
                         import lombok.Builder;
                         import lombok.Data;
                         import lombok.NoArgsConstructor;
                         @Data
                         @Builder
                         @NoArgsConstructor
                         @AllArgsConstructor
                         public class ConversationSession {
                         private String sessionId;
                         private String userId;
                         private Long createdAt;
                         private Long lastActivity;
                         }
                         EOF
                         cat > "$JAVA_BASE/conversation/model/UserContext.java" << 'EOF'
                         package com.jk.labs.spring_ai.pet_care.conversation.model;
                         import lombok.AllArgsConstructor;
                         import lombok.Builder;
                         import lombok.Data;
                         import lombok.NoArgsConstructor;
                         @Data
                         @Builder
                         @NoArgsConstructor
                         @AllArgsConstructor
                         public class UserContext {
                         private String userId;
                         private String sessionId;
                         private Object preferences;
                         }
                         EOF
                         cat > "$JAVA_BASE/conversation/controller/ChatController.java" << 'EOF'
                         package com.jk.labs.spring_ai.pet_care.conversation.controller;
                         import com.jk.labs.spring_ai.pet_care.conversation.service.ConversationService;
                         import lombok.RequiredArgsConstructor;
                         import lombok.extern.slf4j.Slf4j;
                         import org.springframework.web.bind.annotation.*;
                         @Slf4j
                         @RestController
                         @RequestMapping("/api/chat")
                         @RequiredArgsConstructor
                         public class ChatController {

                          private final ConversationService conversationService;private final ConversationService conversationService;

                          // TODO: Implement chat endpoints
                          }
                          EOF
                          cat > "$JAVA_BASE/conversation/config/ConversationConfig.java" << 'EOF'
                          package com.jk.labs.spring_ai.pet_care.conversation.config;
                          import lombok.Data;
                          import org.springframework.boot.context.properties.ConfigurationProperties;
                          import org.springframework.context.annotation.Configuration;
                          @Data
                          @Configuration
                          @ConfigurationProperties(prefix = "conversation")
                          public class ConversationConfig {
                          private Integer maxHistorySize = 10;
                          private Long sessionTimeout = 3600000L; // 1 hour
                          }
                          EOF
                          ==================== RESOURCES ====================
                          echo -e "GREENCreatingResourceFiles...{GREEN}Creating Resource Files...
                          GREENCreatingResourceFiles...{NC}"

                          application.yml
                          cat > "$RESOURCES_BASE/application.yml" << 'EOF'
                          spring:
                          application:
                          name: petcare-catalog-ai-agent
                          profiles:
                          active: dev
                          ai:
                          openai:
                          api-key: ${OPENAI_API_KEY}
                          chat:
                          options:
                          model: gpt-4o-mini
                          temperature: 0.7
                          max-tokens: 2000
                          server:
                          port: 8083
                          mcp:
                          client:
                          base-url: http://localhost:8082
                          timeout: 30000
                          cache:
                          enabled: true
                          default-ttl: 3600
                          vector:
                          db:
                          enabled: false
                          provider: chromadb
                          conversation:
                          max-history-size: 10
                          session-timeout: 3600000
                          orchestration:
                          strategy: AUTO
                          max-agent-calls: 5
                          enable-handoff: true
                          Agent Configurations
                          agent:
                          advisor:
                          enabled: true
                          model: gpt-4o-mini
                          temperature: 0.7
                          research:
                          enabled: true
                          model: gpt-4o-mini
                          temperature: 0.3
                          recommendation:
                          enabled: true
                          model: gpt-4o
                          temperature: 0.5
                          comparison:
                          enabled: true
                          model: gpt-4o-mini
                          temperature: 0.4
                          sales:
                          enabled: true
                          model: gpt-4o-mini
                          temperature: 0.6
                          management:
                          endpoints:
                          web:
                          exposure:
                          include: health,info,metrics
                          base-path: /actuator
                          logging:
                          level:
                          root: INFO
                          com.jk.labs.spring_ai.pet_care: DEBUG
                          EOF
                          application-dev.yml
                          cat > "$RESOURCES_BASE/application-dev.yml" << 'EOF'
                          spring:
                          ai:
                          openai:
                          chat:
                          options:
                          model: gpt-4o-mini
                          logging:
                          level:
                          org.springframework.ai: DEBUG
                          EOF
                          Prompt files
                          mkdir -p "$RESOURCES_BASE/prompts"
                          cat > "$RESOURCES_BASE/prompts/advisor-system-prompt.txt" << 'EOF'
                          You are a helpful and knowledgeable pet care advisor. You help pet owners find the best wellness packages for their pets based on their needs, budget, and pet's age and health conditions. Always be friendly, professional, and focused on the pet's wellbeing.
                          EOF
                          cat > "$RESOURCES_BASE/prompts/research-system-prompt.txt" << 'EOF'
                          You are a research specialist focused on gathering accurate information about pet care packages and services. Provide factual, detailed information to help users make informed decisions.
                          EOF
                          cat > "$RESOURCES_BASE/prompts/recommendation-system-prompt.txt" << 'EOF'
                          You are a recommendation expert who analyzes pet profiles and suggests the most suitable wellness packages. Consider the pet's age, health, and owner's budget when making recommendations.
                          EOF
                          cat > "$RESOURCES_BASE/prompts/comparison-system-prompt.txt" << 'EOF'
                          You are a comparison analyst who helps users understand the differences between various pet care packages. Present clear, objective comparisons.
                          EOF
                          cat > "$RESOURCES_BASE/prompts/sales-system-prompt.txt" << 'EOF'
                          You are a helpful sales assistant who explains pricing, savings, and package benefits. Be honest and transparent about costs and value.
                          EOF
                          echo -e "\nBLUE========================================{BLUE}========================================
                          BLUE========================================{NC}"
                          echo -e "GREEN✅StructureCreatedSuccessfully!{GREEN}✅ Structure Created Successfully!
                          GREEN✅StructureCreatedSuccessfully!{NC}"
                          echo -e "BLUE========================================{BLUE}========================================
                          BLUE========================================{NC}\n"

                          echo -e "GREENSummary:{GREEN}Summary:
                          GREENSummary:{NC}"
                          echo -e "  📦 Main Application: Created"
                          echo -e "  🔧 Common Infrastructure: Created"
                          echo -e "    - models/ (AI/LLM)"
                          echo -e "    - cache/"
                          echo -e "    - vector/"
                          echo -e "    - memory/"
                          echo -e "    - mcp/"
                          echo -e "    - exception/"
                          echo -e "    - util/"
                          echo -e "    - constant/"
                          echo -e "  🤖 Business Features: Created"
                          echo -e "    - advisor/"
                          echo -e "    - research/"
                          echo -e "    - recommendation/"
                          echo -e "    - comparison/"
                          echo -e "    - sales/"
                          echo -e "    - orchestration/"
                          echo -e "    - conversation/"
                          echo -e "  📄 Resources: Created"
                          echo -e "    - application.yml"
                          echo -e "    - application-dev.yml"
                          echo -e "    - prompts/*.txt"

                          echo -e "\nBLUENextSteps:{BLUE}Next Steps:
                          BLUENextSteps:{NC}"
                          echo -e "  1. Review generated structure"
                          echo -e "  2. Implement business logic in service classes"
                          echo -e "  3. Add OpenAI API key to environment"
                          echo -e "  4. Run: mvn clean install"
                          echo -e "  5. Run: mvn spring-boot
                          :run\n"