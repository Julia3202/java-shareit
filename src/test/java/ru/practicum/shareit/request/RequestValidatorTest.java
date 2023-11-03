package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.dto.RequestDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestValidatorTest {
    private final RequestValidator requestValidator = new RequestValidator();
    private int from = 0;
    private int size = 1;


    @Test
    void validDescription() {
        RequestDto requestDto = new RequestDto(1L, "requestDescription", null, LocalDateTime.now());
        Boolean count = requestValidator.validDescription(requestDto);
        assertEquals(true, count);
        requestDto.setDescription("");
        try {
            requestValidator.validDescription(requestDto);
        } catch (ValidationException exception) {
            assertEquals("Поле с описанием обязательно к заполнению.", exception.getMessage());
        }
    }

    @Test
    void validFrom() {
        Boolean count = requestValidator.validFrom(from);
        assertEquals(true, count);
        from = -1;
        try {
            requestValidator.validFrom(from);
        } catch (ValidationException exception) {
            assertEquals("Значение первого элемента должно быть строго больше 0.", exception.getMessage());
        }
    }

    @Test
    void validSize() {
        Boolean count = requestValidator.validSize(size);
        assertEquals(true, count);
        size = 0;
        try {
            requestValidator.validSize(size);
        } catch (ValidationException exception) {
            assertEquals("Количество выводимых строк строго должно быть больше 0.", exception.getMessage());
        }
    }
}