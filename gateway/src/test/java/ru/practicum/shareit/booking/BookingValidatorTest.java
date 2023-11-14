package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.BookingDtoItem;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.ValidationException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingValidatorTest {
    private final BookingValidator bookingValidator = new BookingValidator();
    private BookingDtoItem bookingDtoItem;

    @BeforeEach
    void beforeEach() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusDays(5);
        bookingDtoItem = new BookingDtoItem(1, start, end, 1L, Status.APPROVED);
    }

    @Test
    void validStartBooking() {
        bookingDtoItem.setStart(null);
        try {
            bookingValidator.validStartBooking(bookingDtoItem);
        } catch (ValidationException exception) {
            assertEquals("Поля с датой начала аренды обязательны к заполнению.", exception.getMessage());
        }
        bookingDtoItem.setStart(LocalDateTime.now().minusDays(1));
        try {
            bookingValidator.validStartBooking(bookingDtoItem);
        } catch (ValidationException exception) {
            assertEquals("Укажите желаемую дату бронирования.", exception.getMessage());
        }
    }

    @Test
    void validEndBooking() {
        bookingDtoItem.setEnd(null);
        try {
            bookingValidator.validEndBooking(bookingDtoItem);
        } catch (ValidationException exception) {
            assertEquals("Поля с датой окончания аренды обязательны к заполнению.", exception.getMessage());
        }
        bookingDtoItem.setEnd(bookingDtoItem.getStart().minusDays(1));
        try {
            bookingValidator.validEndBooking(bookingDtoItem);
        } catch (ValidationException exception) {
            assertEquals("Неверно указана дата или время возврата вещи. Дата и время окончания брони " +
                            "должно быть позже даты начала бронирования, поправьте данные и попробуйте повторить попытку.",
                    exception.getMessage());
        }
    }

    @Test
    void validBooking() {
        Boolean count = bookingValidator.validBooking(bookingDtoItem);
        assertEquals(true, count);
    }
}