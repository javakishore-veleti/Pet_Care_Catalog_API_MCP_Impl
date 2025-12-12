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




