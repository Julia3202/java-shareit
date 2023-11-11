package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Slf4j
public class BookingValidator {
    public Boolean validUserForBooking(Item item, User user) {
        if (item.getOwner().equals(user)) {
            throw new NotFoundException("Бронирование невозможно. Вещь принадлежить человеку, отправившему запрос.");
        }
        return true;
    }
}
