package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.ItemDto;

@Slf4j
public class ItemValidator {
    public boolean validName(ItemDto item) {
        if (StringUtils.isBlank(item.getName())) {
            log.error("Поле с названием не заполнено.");
            throw new ValidationException("Заполните поле с названием.");
        }
        return true;
    }

    public boolean validDescription(ItemDto item) {
        if (StringUtils.isBlank(item.getDescription())) {
            log.error("Поле с описание не заполнено.");
            throw new ValidationException("Заполните поле - описание.");
        }
        return true;
    }

    public boolean validAvailable(ItemDto item) {
        if (item.getAvailable() == null) {
            throw new ValidationException("Не заполнено поле со статусом о доступности вещи.");
        }
        return true;
    }

    public boolean validItem(ItemDto item) {
        return validDescription(item) && validName(item) && validAvailable(item);
    }
}
