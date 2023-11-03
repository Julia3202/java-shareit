package ru.practicum.shareit.request;

import org.apache.commons.lang3.StringUtils;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.dto.RequestDto;

public class RequestValidator {
    public Boolean validDescription(RequestDto requestDto) {
        if (StringUtils.isBlank(requestDto.getDescription())) {
            throw new ValidationException("Поле с описанием обязательно к заполнению.");
        }
        return true;
    }

    public Boolean validFrom(int from) {
        if (from < 0) {
            throw new ValidationException("Значение первого элемента должно быть строго больше 0.");
        }
        return true;
    }

    public Boolean validSize(int size) {
        if (size <= 0) {
            throw new ValidationException("Количество выводимых строк строго должно быть больше 0.");
        }
        return true;
    }
}
