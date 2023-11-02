package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoItem;

import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    public static final String USER_ID = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestHeader(USER_ID) long userId, @RequestBody BookingDtoItem bookingDto) {
        return bookingService.saveBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto bookingApproved(@RequestHeader(USER_ID) long userId,
                                      @PathVariable long bookingId, @RequestParam boolean approved) {
        return bookingService.bookingApproved(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findBookingById(@RequestHeader(USER_ID) long userId,
                                      @PathVariable long bookingId) {
        return bookingService.findBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> findUsersBookings(@RequestHeader(USER_ID) long userId,
                                              @RequestParam(defaultValue = "ALL") String state,
                                              @RequestParam(defaultValue = "0") int from,
                                              @RequestParam(defaultValue = "20") int size) {
        return bookingService.findUsersBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> findOwnersBookings(@RequestHeader(USER_ID) long userId,
                                               @RequestParam(defaultValue = "ALL") String state,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "20") int size) {
        return bookingService.findOwnersBookings(userId, state, from, size);
    }
}
