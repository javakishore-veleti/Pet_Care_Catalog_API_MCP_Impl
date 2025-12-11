package com.jk.labs.spring_ai.pet_care.catalog.common.enums;

public enum PetType {
    DOG("Dog"),
    CAT("Cat");

    private final String displayName;

    PetType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
