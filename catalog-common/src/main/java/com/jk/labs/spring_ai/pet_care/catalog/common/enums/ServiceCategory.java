package com.jk.labs.spring_ai.pet_care.catalog.common.enums;

public enum ServiceCategory {

    VACCINATION("Vaccination"),
    DIAGNOSTIC("Diagnostic Testing"),
    DENTAL("Dental Care"),
    SURGERY("Surgery"),
    VIRTUAL_CARE("Virtual Care"),
    WELLNESS("Wellness & Prevention"),
    PREVENTIVE("Preventive Care");

    private final String displayName;

    ServiceCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
