package com.zidioDev.ExpenseManager.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String format(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(DEFAULT_FORMAT));
    }

    public static LocalDateTime parse(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(DEFAULT_FORMAT));
    }
}



