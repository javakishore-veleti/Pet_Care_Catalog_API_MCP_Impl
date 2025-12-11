package com.jk.labs.spring_ai.pet_care.catalog.common.enums;

public enum AgeGroup {
    PUPPY("Puppy", 0, 6),
    KITTEN("Kitten", 0, 6),
    ADULT("Adult", 6, 84),
    SENIOR("Senior", 84, 300);

    private final String displayName;
    private final int minMonths;
    private final int maxMonths;

    AgeGroup(String displayName, int minMonths, int maxMonths) {
        this.displayName = displayName;
        this.minMonths = minMonths;
        this.maxMonths = maxMonths;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getMinMonths() {
        return minMonths;
    }

    public int getMaxMonths() {
        return maxMonths;
    }
}
