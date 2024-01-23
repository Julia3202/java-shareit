package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

@Slf4j
public class UserValidator {
    public boolean validName(UserDto user) {
        if (StringUtils.isBlank(user.getName())) {
            log.info("Поле с именем должно быть заполнено.");
            throw new ValidationException("Заполните поле с именем.");
        }
        return true;
    }

    public boolean validEmail(UserDto user) throws ValidationException {
        if ((user.getEmail() == null) || (!user.getEmail().contains("@"))) {
            log.warn("Поле 'email' не может быть пустым и должен содержать символ '@'.");
            throw new ValidationException("Поле 'email' не может быть пустым и должен содержать символ '@'.");
        }
        return true;
    }

    public boolean validate(UserDto user) {
        return (validName(user) && (validEmail(user)));
    }
}
