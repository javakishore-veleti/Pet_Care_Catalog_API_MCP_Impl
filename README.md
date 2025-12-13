# Pet Care Catalog API & MCP Implementation

**Enterprise-grade Spring Boot multi-module application** providing Pet Care Product Catalog services via both REST API and MCP (Model Context Protocol) Server.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Module Structure](#module-structure)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [MCP Server](#mcp-server)
- [Development](#development)
- [Deployment](#deployment)
- [Contributing](#contributing)

## Overview

This project demonstrates a modular Spring Boot architecture that exposes pet care wellness packages through:
- **REST API** - Traditional HTTP endpoints for web/mobile applications
- **MCP Server** - Model Context Protocol for AI agent integration
- **Combined Mode** - Run both API and MCP server simultaneously

### Key Features

1. Multi-module Maven architecture  
2. Spring Boot 3.2.6 with Java 21  
3. H2 (dev) and PostgreSQL (prod) databases  
4. Liquibase database migrations  
5. OpenAPI 3.0 documentation  
6. Docker & Docker Compose ready  
7. Prometheus metrics  
8. WireMock for external service mocking  
9. Comprehensive test coverage

## Architecture

### Current Architecture

```text
User Request
    ↓
ChatController (Port 8083)
    ↓
ConversationService
    ↓
Intent Detection
    ↓
McpClientService
    ↓
MCP Server (Port 8082)
    ↓
Tools → PackageService
    ↓
Response
```
```text
Next Steps - Where Do You Want to Go?
Option A: Add Real AI (OpenAI Integration)

Integrate with OpenAI GPT-4
Add function calling
Make conversations truly intelligent
Let AI decide which MCP tools to use

Option B: Enhanced Multi-Agent System

Activate all 5 agents (Advisor, Research, Recommendation, Comparison, Sales)
Build the orchestrator
Add agent handoff
Implement routing strategies

Option C: Add More Features

Conversation memory/history
Vector database for RAG
Caching layer
WebSocket for streaming responses

Option D: Deploy to Cloud

Create Terraform scripts
GitHub Actions workflows
Deploy to AWS/Azure/GCP
Set up CI/CD

Option E: Build Agentic AI Module

Create catalog-agentic-ai module
Multi-agent orchestration with CrewAI
Complex workflow automation
n8n integration

Option B: Enhanced Multi-Agent System
* Activate all 5 agents (Advisor, Research, Recommendation, Comparison, Sales)
* Build the orchestrator
* Add agent handoff
* Implement routing strategies then 
Option C: Add More Features
* Conversation memory/history
* Vector database for RAG
* Caching layer
* WebSocket for streaming responses then 
Option D: Deploy to Cloud
* Create Terraform scripts
* GitHub Actions workflows
* Deploy to AWS/Azure/GCP
* Set up CI/CD then 
Option A: Add Real AI (OpenAI Integration)
* Integrate with OpenAI GPT-4
* Add function calling
* Make conversations truly intelligent
* Let AI decide which MCP tools to use then Option E: Build Agentic AI Module
   * Create catalog-agentic-ai module
   * Multi-agent orchestration with CrewAI
   * Complex workflow automation
   * n8n integration


```

### Module Dependencies

```
catalog-common (Base Layer)
       ↑
       |
catalog-core (Business Logic)
       ↑
       |
    ┌──┴──────────────┬──────────────┐
    |                 |              |
catalog-api    catalog-mcp-server   catalog-api-mcp
```

### Technology Stack

| Layer | Technology |
|-------|-----------|
| **Framework** | Spring Boot 3.2.6 |
| **Language** | Java 21 |
| **Build Tool** | Maven 3.9+ |
| **Database** | PostgreSQL 16 / H2 |
| **Migration** | Liquibase 4.27 |
| **Documentation** | SpringDoc OpenAPI 2.5 |
| **Testing** | JUnit 5, TestContainers, WireMock |
| **Monitoring** | Micrometer, Prometheus |

## Prerequisites

- **Java 21+**
- **Maven 3.9+**
- **Docker & Docker Compose** (for infrastructure)
- **Git**

## Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/Pet-Care-Catalog-API-MCP-Impl.git
cd Pet-Care-Catalog-API-MCP-Impl
```

### 2. Start Infrastructure

```bash
cd DevOps
chmod +x docker-up.sh docker-down.sh docker-status.sh
./docker-up.sh
```

This starts:
- PostgreSQL on port **5432**
- WireMock on port **9090**

### 3. Build All Modules

```bash
cd ..
mvn clean install
```

### 4. Run the Application

**Option A: API Only**
```bash
cd catalog-api
mvn spring-boot:run
```
Access: http://localhost:8080

**Option B: MCP Server Only**
```bash
cd catalog-mcp-server
mvn spring-boot:run
```
Access: http://localhost:8081

**Option C: Combined (API + MCP)**
```bash
cd catalog-api-mcp
mvn spring-boot:run
```
Access: API on 8080, MCP on 8081

## Module Structure

### catalog-common
Shared DTOs, constants, enums, exceptions, and DAO interfaces

**Package**: `com.jk.labs.springai.petcare`

Key components:
- `dto/` - Data Transfer Objects
- `enums/` - PetType, AgeGroup, CareLevel, ServiceCategory
- `exception/` - Custom exceptions
- `constants/` - Application constants
- `util/` - Utility classes

### catalog-core
Core business logic, JPA entities, repository implementations

**Package**: `com.jk.labs.springai.petcare`

Key components:
- `entity/` - JPA entities
- `repository/` - Spring Data JPA repositories
- `service/impl/` - Business logic implementations
- `facade/` - Facade layer for complex operations
- `mapper/` - MapStruct mappers
- `db/changelog/` - Liquibase migrations

### catalog-api
REST API controllers and OpenAPI documentation

**Package**: `com.jk.labs.springai.petcare`

Key components:
- `controller/` - REST controllers
- `config/` - API configuration (OpenAPI, Security)
- `exception/` - Global exception handler

Endpoints:
- `GET /api/v1/packages` - List all packages
- `GET /api/v1/packages/{code}` - Get package details
- `POST /api/v1/recommendations` - Get package recommendation
- `POST /api/v1/packages/compare` - Compare packages

### catalog-mcp-server
MCP Server implementation with AI agent tools

**Package**: `com.jk.labs.springai.petcare`

Key components:
- `mcp/` - MCP protocol implementation
- `mcp/tools/` - MCP tool implementations
- `mcp/schema/` - MCP protocol schemas

MCP Tools:
- `search_packages` - Search for packages
- `get_package_details` - Get detailed package info
- `recommend_package` - AI-powered recommendations
- `compare_packages` - Side-by-side comparison
- `calculate_savings` - Calculate cost savings
- `get_services` - List available services

### catalog-api-mcp
Combined module running both API and MCP Server

## API Documentation

### Swagger UI (Development)

When running in dev mode, access interactive API documentation:

**URL**: http://localhost:8080/swagger-ui.html

### Sample API Requests

**Get All Packages**
```bash
curl -X GET http://localhost:8080/api/v1/packages
```

**Get Package by Code**
```bash
curl -X GET http://localhost:8080/api/v1/packages/DOG_ACTIVE_CARE
```

**Get Recommendation**
```bash
curl -X POST http://localhost:8080/api/v1/recommendations \
  -H "Content-Type: application/json" \
  -d '{
    "petType": "DOG",
    "ageYears": 3,
    "ageMonths": 6,
    "needsDentalCare": true
  }'
```

## MCP Server

### What is MCP?

Model Context Protocol (MCP) enables AI agents to interact with external systems. Our MCP server exposes pet care catalog tools that AI agents can use.

### MCP Tool Usage

**List Available Tools**
```bash
curl -X POST http://localhost:8081/mcp/tools
```

**Execute Tool**
```bash
curl -X POST http://localhost:8081/mcp/tools/search_packages \
  -H "Content-Type: application/json" \
  -d '{
    "petType": "DOG",
    "ageGroup": "ADULT"
  }'
```

### Integrating with AI Agents

The MCP server can be integrated with:
- Claude Desktop
- Custom AI agents
- LangChain applications
- CrewAI multi-agent systems

## Development

### Database Access

**H2 Console** (Development)
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:petcare_catalog`
- Username: `sa`
- Password: (empty)

**PostgreSQL** (Production)
- Host: localhost:5432
- Database: petcare_catalog
- Username: petcare_user
- Password: petcare_password

### Running Tests

```bash
# Run all tests
mvn test

# Run tests for specific module
cd catalog-core
mvn test

# Run integration tests
mvn verify
```

### Code Quality

```bash
# Check code style
mvn checkstyle:check

# Run static analysis
mvn pmd:check
```

### Hot Reload

```bash
mvn spring-boot:run -Dspring-boot.run.fork=false
```

## Docker Deployment

### Build Docker Images

```bash
# Build API image
docker build -f catalog-api/Dockerfile -t petcare-catalog-api:latest .

# Build MCP image
docker build -f catalog-mcp-server/Dockerfile -t petcare-catalog-mcp:latest .
```

### Docker Compose Deployment

```bash
cd DevOps
docker-compose -f docker-compose-all.yml up -d
```

### Kubernetes Deployment

```bash
kubectl apply -f k8s/
```

## Monitoring

### Health Checks

- **API**: http://localhost:8080/actuator/health
- **MCP**: http://localhost:8081/actuator/health

### Prometheus Metrics

- **API**: http://localhost:8080/actuator/prometheus
- **MCP**: http://localhost:8081/actuator/prometheus

## Testing with WireMock

WireMock server runs on port 9090 and provides mock responses for external services.

**Admin UI**: http://localhost:9090/__admin

**Test Mock Endpoints**:
```bash
curl http://localhost:9090/payment/health
curl -X POST http://localhost:9090/payment/process
```

## Available Packages

| Package            | Pet Type | Age Group | Features |
|--------------------|----------|-----------|----------|
| Timely Care        | Dog/Cat | Puppy/Kitten | Basic vaccines, exams |
| Timely Care Plus   | Dog/Cat | Puppy/Kitten | + Spay/Neuter |
| Grown-Up Care      | Dog/Cat | Adult | Wellness exams, diagnostics |
| Grown-Up Care Plus | Dog/Cat | Adult | + Dental cleaning |
| Elder Care         | Dog/Cat | Senior | Advanced diagnostics |
| Elder Care Plus       | Dog/Cat | Senior | + Dental care |
| Spl Care           | All | All | Chronic condition support |


**Made with ❤️ for pets and their humans**

---

# Recommendation Agent Module

## Overview

The **Recommendation Agent** is part of the Pet Care multi-agent system. It analyzes pet profiles and care needs to recommend the most suitable wellness packages.

## Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                    Recommendation Agent                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  RecommendationController                                        │
│         │                                                        │
│         ▼                                                        │
│  RecommendationService                                           │
│         │                                                        │
│    ┌────┴────┐                                                   │
│    │         │                                                   │
│    ▼         ▼                                                   │
│  Research   MCP Client                                           │
│  Service    Service                                              │
│    │         │                                                   │
│    ▼         ▼                                                   │
│  Research   MCP Server                                           │
│  Agent      (recommend_package tool)                             │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## Features

### 1. Full Recommendation
- Comprehensive pet profile analysis
- Considers age, health needs, dental care, chronic conditions
- Returns primary recommendation + alternatives
- Provides reasoning and key benefits

### 2. Quick Recommendation
- Minimal input (pet type + age)
- Fast response for simple queries

### 3. Refine Recommendation
- Update recommendation with additional context
- Session-based continuity

## File Structure

```
recommendation/
├── config/
│   └── RecommendationConfig.java       # Agent configuration properties
├── controller/
│   └── RecommendationController.java   # REST API endpoints
├── model/
│   ├── RecommendationRequest.java      # Input model
│   └── RecommendationResponse.java     # Output model with PackageRecommendation
├── service/
│   ├── RecommendationService.java      # Service interface
│   └── impl/
│       └── RecommendationServiceImpl.java  # Core recommendation logic
└── resources/
    └── application-recommendation.yml  # Configuration
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/agents/recommendation/recommend` | Full recommendation |
| GET | `/api/v1/agents/recommendation/quick` | Quick recommendation |
| POST | `/api/v1/agents/recommendation/refine/{sessionId}` | Refine existing |

## Sample Request

```bash
curl -X POST http://localhost:8083/api/v1/agents/recommendation/recommend \
  -H "Content-Type: application/json" \
  -d '{
    "petType": "DOG",
    "petName": "Buddy",
    "ageYears": 3,
    "ageMonths": 6,
    "needsDentalCare": true,
    "budgetPreference": "STANDARD"
  }'
```

## Sample Response

```json
{
  "agentName": "Recommendation Agent",
  "primaryRecommendation": {
    "packageCode": "DOG_GROWNUP_CARE_PLUS",
    "packageName": "Grown-Up Care Plus",
    "description": "Comprehensive adult dog care with dental coverage",
    "monthlyPrice": 54.99,
    "includedServices": ["Wellness Exams", "Vaccinations", "Dental Cleaning"],
    "matchScore": 0.92
  },
  "alternatives": [
    {
      "packageCode": "DOG_GROWNUP_CARE",
      "packageName": "Grown-Up Care",
      "description": "Standard adult dog care package",
      "monthlyPrice": 39.99,
      "includedServices": ["Wellness Exams", "Vaccinations"],
      "matchScore": 0.78
    }
  ],
  "reasoning": "Based on your adult dog Buddy, I recommend the Grown-Up Care Plus package...",
  "keyBenefits": [
    "Tailored for adult pets",
    "Includes 8 essential services",
    "Dental coverage included"
  ],
  "confidence": 0.85,
  "recommendationType": "BEST_FIT",
  "suggestComparison": true,
  "nextAgentSuggestion": "Comparison Agent"
}
```

## Integration with Other Agents

### Uses Research Agent
The Recommendation Agent calls the Research Agent to gather package data before making recommendations.

### Handoff to Comparison Agent
When multiple packages have high match scores, suggests using the Comparison Agent.

### Handoff to Sales Agent
After recommendation is accepted, can hand off to Sales Agent for purchase flow.

## Configuration

```yaml
agent:
  recommendation:
    enabled: true
    model: gpt-4o-mini
    temperature: 0.5
    min-confidence-threshold: 0.6
    max-alternatives: 3
```

## Next Steps After This Agent

1. **Comparison Agent** - Compare multiple packages side-by-side
2. **Advisor Agent** - General pet care Q&A
3. **Sales Agent** - Handle purchase conversions
4. **Orchestrator** - Route between agents based on intent

---

## Agent Progress Tracker

| Agent | Status | Dependencies |
|-------|--------|--------------|
| Research | ✅ Implemented | MCP Client |
| **Recommendation** | ✅ Implemented | Research, MCP Client |
| Comparison | 🔲 Next | Research, MCP Client |
| Advisor | 🔲 Pending | Research |
| Sales | 🔲 Pending | Recommendation |
| Orchestrator | 🔲 Pending | All agents |


# Comparison Agent Module

## Overview

The **Comparison Agent** is part of the Pet Care multi-agent system. It provides detailed side-by-side comparison of wellness packages, helping users make informed decisions.

## Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                     Comparison Agent                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ComparisonController                                            │
│         │                                                        │
│         ▼                                                        │
│  ComparisonService                                               │
│         │                                                        │
│    ┌────┴────┐                                                   │
│    │         │                                                   │
│    ▼         ▼                                                   │
│  Research   MCP Client                                           │
│  Service    Service                                              │
│    │         │                                                   │
│    ▼         ▼                                                   │
│  Research   MCP Server                                           │
│  Agent      (get_package_details, compare_packages tools)        │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

## Features

### 1. Full Comparison
- Compare 2-4 packages
- Feature matrix (side-by-side)
- Price comparison with savings analysis
- Winner recommendation with reasoning
- Key differences highlighted

### 2. Quick Comparison
- Fast comparison of exactly 2 packages
- Minimal input required

### 3. Focused Comparison
- Specify features to focus on (dental, price, vaccinations)
- Weighted scoring based on priorities

### 4. Pet-Specific Comparison
- Consider pet type and age
- Recommend best package for specific pet

## File Structure

```
comparison/
├── config/
│   └── ComparisonConfig.java         # Agent configuration properties
├── controller/
│   └── ComparisonController.java     # REST API endpoints
├── model/
│   ├── ComparisonRequest.java        # Input model
│   └── ComparisonResponse.java       # Output with all comparison details
├── service/
│   ├── ComparisonService.java        # Service interface
│   └── impl/
│       └── ComparisonServiceImpl.java    # Core comparison logic
└── resources/
    └── application-comparison.yml    # Configuration
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/agents/comparison/compare` | Full comparison |
| GET | `/api/v1/agents/comparison/quick` | Quick 2-package comparison |
| GET | `/api/v1/agents/comparison/multiple` | Multiple packages |
| POST | `/api/v1/agents/comparison/focus` | Compare with focus areas |
| GET | `/api/v1/agents/comparison/for-pet` | Pet-specific comparison |

## Sample Request

```bash
curl -X POST http://localhost:8083/api/v1/agents/comparison/compare \
  -H "Content-Type: application/json" \
  -d '{
    "packageCodes": ["DOG_GROWNUP_CARE", "DOG_GROWNUP_CARE_PLUS", "DOG_ELDER_CARE"],
    "petType": "DOG",
    "petAgeYears": 5,
    "priorities": ["dental", "value"],
    "maxBudget": 60.00
  }'
```

## Sample Response

```json
{
  "agentName": "Comparison Agent",
  "packages": [
    {
      "packageCode": "DOG_GROWNUP_CARE",
      "packageName": "Grown-Up Care",
      "monthlyPrice": 39.99,
      "includesDental": false,
      "valueScore": 0.8
    },
    {
      "packageCode": "DOG_GROWNUP_CARE_PLUS",
      "packageName": "Grown-Up Care Plus",
      "monthlyPrice": 54.99,
      "includesDental": true,
      "valueScore": 0.85
    }
  ],
  "featureMatrix": {
    "features": {
      "Monthly Price": { "DOG_GROWNUP_CARE": "$39.99", "DOG_GROWNUP_CARE_PLUS": "$54.99" },
      "Dental Coverage": { "DOG_GROWNUP_CARE": "✗", "DOG_GROWNUP_CARE_PLUS": "✓" }
    }
  },
  "priceComparison": {
    "cheapestPackage": "DOG_GROWNUP_CARE",
    "mostExpensivePackage": "DOG_GROWNUP_CARE_PLUS",
    "priceDifference": 15.00,
    "bestValuePackage": "DOG_GROWNUP_CARE_PLUS"
  },
  "winner": {
    "packageCode": "DOG_GROWNUP_CARE_PLUS",
    "packageName": "Grown-Up Care Plus",
    "winReason": "Best overall balance considering your priorities: dental, value",
    "advantages": ["Includes dental coverage", "Excellent value for money"],
    "disadvantages": ["Higher price point than alternatives"],
    "bestForBudget": "DOG_GROWNUP_CARE",
    "bestForCoverage": "DOG_GROWNUP_CARE_PLUS",
    "bestForValue": "DOG_GROWNUP_CARE_PLUS"
  },
  "summary": "Comparing 2 packages: Grown-Up Care, Grown-Up Care Plus...",
  "keyDifferences": [
    "Price range: $39.99 to $54.99 per month",
    "1 of 2 packages include dental coverage"
  ],
  "confidence": 0.85,
  "nextAgentSuggestion": "Sales Agent"
}
```

## Response Components

### PackageComparison
Details for each package being compared.

### FeatureMatrix
Side-by-side grid showing features across all packages.

### PriceComparison
Price analysis including cheapest, most expensive, and best value.

### ComparisonWinner
Recommended package with reasoning, advantages, and category winners.

## Integration with Other Agents

### Called By
- **Recommendation Agent** - When multiple good matches are found
- **Advisor Agent** - When user wants to compare options

### Calls
- **Research Agent** - To gather package data if MCP fails
- **MCP Client** - For package details

### Hands Off To
- **Sales Agent** - After user decides on a package

## Configuration

```yaml
agent:
  comparison:
    enabled: true
    max-packages-to-compare: 4
    min-packages-to-compare: 2
    price-weight: 0.3
    coverage-weight: 0.4
    value-weight: 0.3
```

---

## Agent Progress Tracker

| Agent | Status | Dependencies |
|-------|--------|--------------|
| Research | ✅ Implemented | MCP Client |
| Recommendation | ✅ Implemented | Research, MCP Client |
| **Comparison** | ✅ Implemented | Research, MCP Client |
| Advisor | 🔲 Next | Research, Recommendation |
| Sales | 🔲 Pending | Recommendation, Comparison |
| Orchestrator | 🔲 Pending | All agents |

---

## Next Steps

1. **Advisor Agent** - General pet care Q&A and entry point
2. **Sales Agent** - Handle purchase flow
3. **Orchestrator** - Route between agents based on intent


Next Agent?
Ready to build the next agent. Which would you prefer?

Advisor Agent - General pet care Q&A, entry point for conversations
Sales Agent - Handle purchase flow after recommendation/comparison

