package com.jk.labs.spring_ai.pet_care.catalog.common.enums;

public enum CareLevel {

    EARLY_CARE("Early Care", 1),
    EARLY_CARE_PLUS("Early Care Plus", 2),
    ACTIVE_CARE("Active Care", 3),
    ACTIVE_CARE_PLUS("Active Care Plus", 4),
    SENIOR_CARE("Senior Care", 5),
    SENIOR_CARE_PLUS("Senior Care Plus", 6),
    SPECIAL_CARE("Special Care", 7);

    private final String displayName;
    private final int level;

    CareLevel(String displayName, int level) {
        this.displayName = displayName;
        this.level = level;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getLevel() {
        return level;
    }
}
