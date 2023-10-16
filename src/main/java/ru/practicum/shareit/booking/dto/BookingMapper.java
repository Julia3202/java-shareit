package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus()
        );
    }

    public static Booking toBooking(BookingDto bookingDto) {
        return new Booking(
                bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                bookingDto.getItem(),
                bookingDto.getBooker(),
                bookingDto.getStatus()
        );
    }

    public static Booking toBooking(BookingDto bookingDto, Booking booking) {
        return new Booking(
                bookingDto.getId(),
                bookingDto.getStart() != null ? bookingDto.getStart() : booking.getStart(),
                bookingDto.getEnd() != null ? bookingDto.getEnd() : booking.getEnd(),
                bookingDto.getItem() != null ? bookingDto.getItem() : booking.getItem(),
                bookingDto.getBooker() != null ? bookingDto.getBooker() : booking.getBooker(),
                bookingDto.getStatus() != null ? bookingDto.getStatus() : booking.getStatus()
        );
    }
}
