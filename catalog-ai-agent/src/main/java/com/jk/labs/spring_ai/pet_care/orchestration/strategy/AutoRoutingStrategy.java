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