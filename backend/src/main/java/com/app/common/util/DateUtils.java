package com.app.common.util;

import com.app.exception.DateFormatException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static String formatDate(LocalDateTime date) throws DateFormatException {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            return date.format(formatter);
        } catch (Exception e) {
            throw new DateFormatException();
        }
    }

    public static String formatDate(LocalDate date) throws DateFormatException {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return date.format(formatter);
        } catch (Exception e) {
            throw new DateFormatException();
        }
    }
}
