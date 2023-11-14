package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DateStatusTest {

    @Test
    void values() {
        DateStatus[] dateStatuses = DateStatus.values();
        assertEquals(6, dateStatuses.length);
    }

    @Test
    void valueOf() {
        String stringStatus = "ALL";
        DateStatus status = DateStatus.valueOf(stringStatus);
        assertEquals(DateStatus.ALL, status);
    }
}