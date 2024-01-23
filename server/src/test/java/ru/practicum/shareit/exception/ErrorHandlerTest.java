package ru.practicum.shareit.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorHandlerTest {
    private ErrorHandler errorHandler;
    private ErrorResponse errorResponse;

    @BeforeEach
    void beforeEach() {
        errorHandler = new ErrorHandler();
    }

    @Test
    void handleException() {
        String message = "Произошла внутренняя ошибка сервера.";
        OtherException exception = new OtherException(message);
        errorResponse = errorHandler.handleException(exception);
        assertEquals(message, errorResponse.getError());
    }

    @Test
    void handleNotFoundException() {
        String message = "Искомый объект не найден.";
        NotFoundException exception = new NotFoundException(message);
        errorResponse = errorHandler.handleNotFoundException(exception);
        assertEquals(message, errorResponse.getError());
    }

    @Test
    void handleBadRequest() {
        String message = "Некорректный запрос.";
        ValidationException exception = new ValidationException(message);
        errorResponse = errorHandler.handleBadRequest(exception);
        assertEquals(message, errorResponse.getError());
    }
}