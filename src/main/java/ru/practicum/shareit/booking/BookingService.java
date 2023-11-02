package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoItem;

import java.util.List;

interface BookingService {
    BookingDto saveBooking(long userId, BookingDtoItem bookingDto);

    BookingDto bookingApproved(long userId, long bookingId, boolean approved);

    BookingDto findBookingById(long userId, long bookingId);

    List<BookingDto> findUsersBookings(long userId, String state, int from, int size);

    List<BookingDto> findOwnersBookings(long userId, String state, int from, int size);
}
