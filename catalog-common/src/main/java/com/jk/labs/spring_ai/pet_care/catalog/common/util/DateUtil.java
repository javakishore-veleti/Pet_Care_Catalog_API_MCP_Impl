package com.jk.labs.spring_ai.pet_care.catalog.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtil {

    private static final DateTimeFormatter ISO_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private DateUtil() {
        throw new UnsupportedOperationException("This is a utility class");
    }

    public static String formatToIso(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(ISO_FORMATTER) : null;
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }
}