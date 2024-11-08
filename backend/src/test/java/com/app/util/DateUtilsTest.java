package com.app.util;

import com.app.common.util.DateUtils;
import com.app.exception.DateFormatException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class DateUtilsTest {

    @Test
    void testFormatDateWithLocalDateTime() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 11, 7, 14, 30, 45);
        String expected = "2024-11-07T14:30:45";

        try {
            String formattedDate = DateUtils.formatDate(dateTime);
            assertEquals(expected, formattedDate);
        } catch (DateFormatException e) {
            fail("An unexpected exception was thrown: " + e.getMessage());
        }
    }

    @Test
    void testFormatDateWithLocalDate() {
        LocalDate date = LocalDate.of(2024, 11, 7);
        String expected = "2024-11-07";

        try {
            String formattedDate = DateUtils.formatDate(date);
            assertEquals(expected, formattedDate);
        } catch (DateFormatException e) {
            fail("An unexpected exception was thrown: " + e.getMessage());
        }
    }

    @Test
    void testFormatDateWithNullLocalDateTime() {
        assertThrows(DateFormatException.class, () -> DateUtils.formatDate((LocalDateTime) null));
    }

    @Test
    void testFormatDateWithNullLocalDate() {
        assertThrows(DateFormatException.class, () -> DateUtils.formatDate((LocalDate) null));
    }
}
