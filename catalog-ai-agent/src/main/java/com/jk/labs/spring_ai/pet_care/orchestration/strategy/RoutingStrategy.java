package com.jk.labs.spring_ai.pet_care.orchestration.strategy;
public interface RoutingStrategy {
    String determineAgent(String query);
}