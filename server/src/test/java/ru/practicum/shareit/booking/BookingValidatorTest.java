package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingValidatorTest {
    private final BookingValidator bookingValidator = new BookingValidator();
    private Item item;
    private User user;
    private User userTest;

    @BeforeEach
    void beforeEach() {
        user = new User(1, "userName", "user@mail.ru");
        item = new Item(1, "itemName", "itemDescription", true, user, null);
        userTest = new User(2, "user2Name", "user2@mail.ru");
    }

    @Test
    void validUserForBooking() {
        try {
            bookingValidator.validUserForBooking(item, user);
        } catch (NotFoundException exception) {
            assertEquals("Бронирование невозможно. Вещь принадлежить человеку, отправившему запрос.",
                    exception.getMessage());
        }
        Boolean count = bookingValidator.validUserForBooking(item, userTest);
        assertEquals(true, count);
    }
}