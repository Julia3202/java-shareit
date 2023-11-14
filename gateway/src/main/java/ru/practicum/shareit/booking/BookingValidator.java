package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.model.BookingDtoItem;
import ru.practicum.shareit.booking.model.DateStatus;
import ru.practicum.shareit.exception.ValidationException;

import java.time.LocalDateTime;

import static ru.practicum.shareit.booking.model.DateStatus.*;

@Slf4j
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
                    "должно быть позже даты начала бронирования, поправьте данные и попробуйте повторить попытку.");
        }
        return true;
    }

    public Boolean validState(String state) {
        DateStatus status = DateStatus.valueOf(state);
        if (status != ALL && status != CURRENT && status != PAST && status != FUTURE && status != WAITING && status != REJECTED) {
            throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
        return true;
    }

    public Boolean validBooking(BookingDtoItem bookingDtoItem) {
        return validStartBooking(bookingDtoItem) && validEndBooking(bookingDtoItem);
    }
}
