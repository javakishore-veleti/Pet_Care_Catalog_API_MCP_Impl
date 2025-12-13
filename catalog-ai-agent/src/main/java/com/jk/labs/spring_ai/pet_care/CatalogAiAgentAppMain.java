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
