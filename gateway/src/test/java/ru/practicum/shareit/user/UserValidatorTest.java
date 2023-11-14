package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserValidatorTest {
    private final UserValidator userValidator = new UserValidator();
    private UserDto user;

    @BeforeEach
    void beforeEach() {
        user = new UserDto(1, "userName", "user@mail.ru");
    }

    @Test
    void validName() {
        Boolean count = userValidator.validName(user);
        assertEquals(true, count);
        user.setName("");
        try {
            userValidator.validName(user);
        } catch (ValidationException exception) {
            assertEquals("Заполните поле с именем.", exception.getMessage());
        }
    }

    @Test
    void validEmail() {
        Boolean count = userValidator.validEmail(user);
        assertEquals(true, count);
        user.setEmail("");
        try {
            userValidator.validEmail(user);
        } catch (ValidationException exception) {
            assertEquals("Поле 'email' не может быть пустым и должен содержать символ '@'.",
                    exception.getMessage());
        }
    }

    @Test
    void validate() {
        Boolean count = userValidator.validate(user);
        assertEquals(true, count);
    }
}