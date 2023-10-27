package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class BookingValidator {
    public Boolean validStartBooking(BookingDtoItem bookingDtoItem) {
        if (bookingDtoItem.getStart() == null) {
            throw new ValidationException("Поля с датой начала аренды обязательны к заполнению.");
        }
        if (bookingDtoItem.getStart().toString().isBlank() || bookingDtoItem.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Укажите желаемую дату бронирования.");
        }
        return true;
    }

    public Boolean validEndBooking(BookingDtoItem bookingDtoItem) {
        if (bookingDtoItem.getEnd() == null) {
            throw new ValidationException("Поля с датой окончания аренды обязательны к заполнению.");
        }
        if (bookingDtoItem.getEnd().isBefore(bookingDtoItem.getStart()) ||
                bookingDtoItem.getEnd().equals(bookingDtoItem.getStart())) {
            throw new ValidationException("Неверно указана дата или время возврата вещи. Дата и время окончания брони " +
                    "должно быть позже даты начала бронирования, поправьте данные и попробуйте повторить ошибку.");
        }
        return true;
    }

    public Boolean validUserForBooking(Item item, User user) {
        if (item.getOwner().equals(user)) {
            throw new NotFoundException("Бронирование невозможно. Вещь принадлежить человеку, отправившему запрос.");
        }
        return true;
    }

    public Boolean validBooking(BookingDtoItem bookingDtoItem) {
        return validStartBooking(bookingDtoItem) && validEndBooking(bookingDtoItem);
    }
}
