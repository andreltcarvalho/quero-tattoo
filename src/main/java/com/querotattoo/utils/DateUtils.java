package com.querotattoo.utils;

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

public class DateUtils {

    public static Instant getNow() {
        return Instant.now().minus(3, ChronoUnit.HOURS).with(ChronoField.NANO_OF_SECOND, 0);
    }
}
