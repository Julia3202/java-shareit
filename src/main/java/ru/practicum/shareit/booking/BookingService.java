package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

interface BookingService {
    BookingDto saveBooking(long userId, BookingDto bookingDto);

    BookingDto bookingApproved(long userId, long bookingId, boolean approved);

    BookingDto findBookingById(long userId, long bookingId);

    List<BookingDto> findUsersBookings(long userId, String state);

    List<BookingDto> findOwnersBookings(long userId, String state);
}
