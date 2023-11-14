package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorResponseTest {

    @Test
    void getError() {
        ErrorResponse errorResponse = new ErrorResponse("Error!");
        String error = errorResponse.getError();
        assertEquals("Error!", error);
    }
}