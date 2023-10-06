package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

@Slf4j
public class UserValidator {
    public boolean validName(User user) {
        if (user.getName().isBlank()) {
            log.info("Поле с именем должно быть заполнено.");
            throw new ValidationException("Заполните поле с именем.");
        }
        return true;
    }

    public boolean validEmail(User user) throws ValidationException {
        if ((user.getEmail() == null) || (!user.getEmail().contains("@"))) {
            log.warn("Поле 'email' не может быть пустым и должен содержать символ '@'.");
            throw new ValidationException("Поле 'email' не может быть пустым и должен содержать символ '@'.");
        }
        return true;
    }

    public boolean validate(User user) {
        return (validName(user) && (validEmail(user)));
    }
}
