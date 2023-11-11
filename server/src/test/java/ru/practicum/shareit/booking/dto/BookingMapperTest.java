package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingMapperTest {
    private Item item;
    private User user;
    private Booking booking;
    private BookingDtoItem bookingDtoItem;

    @BeforeEach
    void beforeEach() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(5);
        user = new User(1, "userName", "user@mail.ru");
        Request request = new Request(1, "requestDescription", user, LocalDateTime.now());
        item = new Item(1, "itemName", "itemDescription", true, user, request);
        booking = new Booking(1, start, end, item, user, Status.APPROVED);
        bookingDtoItem = new BookingDtoItem(1, start, end, item.getId(), Status.APPROVED);
    }

    @Test
    void toBookingDto() {
        Booking bookings = null;
        assertNull(BookingMapper.toItemDtoBooking(bookings));
        BookingDto bookingDto = BookingMapper.toBookingDto(booking);
        assertEquals(booking.getId(), bookingDto.getId());
        assertNotNull(bookingDto.getStart());
        assertNotNull(bookingDto.getEnd());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());
        assertEquals(booking.getItem().getName(), bookingDto.getItem().getName());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getStatus(), bookingDto.getStatus());
    }

    @Test
    void toBooking() {
        Booking bookingFromTest = BookingMapper.toBooking(bookingDtoItem, item, user);
        assertEquals(bookingFromTest.getId(), bookingDtoItem.getId());
        assertNotNull(booking.getStart());
        assertNotNull((booking.getEnd()));
        assertEquals(bookingFromTest.getItem().getId(), bookingDtoItem.getItemId());
        assertEquals(bookingFromTest.getStatus(), bookingDtoItem.getStatus());
    }

    @Test
    void toItemDtoBooking() {
        BookingDtoForItem itemDtoBookingTest = BookingMapper.toItemDtoBooking(booking);
        assertEquals(booking.getId(), itemDtoBookingTest.getId());
        assertNotNull(itemDtoBookingTest.getStart());
        assertNotNull(itemDtoBookingTest.getEnd());
        assertEquals(booking.getBooker().getId(), itemDtoBookingTest.getBookerId());
        assertEquals(booking.getStatus(), itemDtoBookingTest.getStatus());
        Booking bookings = null;
        assertNull(BookingMapper.toItemDtoBooking(bookings));
    }
}