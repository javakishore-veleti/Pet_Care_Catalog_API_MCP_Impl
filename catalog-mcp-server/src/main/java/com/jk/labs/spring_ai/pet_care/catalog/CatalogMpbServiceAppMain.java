package com.jk.labs.spring_ai.pet_care.catalog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class CatalogMpbServiceAppMain {

    public static void main(String[] args) {
        SpringApplication.run(CatalogMpbServiceAppMain.class, args);

        System.out.println("""
            
            ╔════════════════════════════════════════════════════════════╗
            ║     Pet Care Catalog - MCP Server Started Successfully     ║
            ║                                                            ║
            ║  MCP Server running on: http://localhost:8082             ║
            ║  Health Check: http://localhost:8082/actuator/health      ║
            ║  MCP Tools: http://localhost:8082/mcp/tools               ║
            ║                                                            ║
            ║  Available MCP Tools:                                      ║
            ║    • search_packages                                       ║
            ║    • get_package_details                                   ║
            ║    • recommend_package                                     ║
            ║    • compare_packages                                      ║
            ║    • calculate_savings                                     ║
            ║    • get_services                                          ║
            ╚════════════════════════════════════════════════════════════╝
            """);
    }
}
