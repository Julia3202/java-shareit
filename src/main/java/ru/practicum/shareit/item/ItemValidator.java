package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;

@Slf4j
@RequiredArgsConstructor
public class ItemValidator {
    public boolean validName(Item item) {
        if (StringUtils.isBlank(item.getName())) {
            log.info("Поле с названием не заполнено.");
            throw new ValidationException("Заполните поле с названием.");
        }
        return true;
    }

    public boolean validDescription(Item item) {
        if (StringUtils.isBlank(item.getDescription())) {
            log.info("Поле с описание не заполнено.");
            throw new ValidationException("Заполните поле - описание.");
        }
        return true;
    }

    public boolean validAvailable(Item item) {
        Boolean available = item.getAvailable();
        if (available == null) {
            throw new ValidationException("Поле со статусом доступа пусто.");
        }
        if (!available) {
            throw new ValidationException("Вещь не доступна для бронирования.");
        }
        return true;
    }

    public boolean validItem(Item item) {
        if (!(validDescription(item) && validName(item))) {
            log.info("Проверьте правильность заполненных данных.");
            throw new ValidationException("Ошибка валидации. Ошибка при заполнении одного из полей.");
        }
        return true;
    }
}
