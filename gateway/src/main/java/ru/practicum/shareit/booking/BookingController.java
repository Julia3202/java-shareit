package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.BookingDtoItem;
import ru.practicum.shareit.booking.model.DateStatus;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.RequestValidator;


@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    public static final String USER_ID = "X-Sharer-User-Id";
    private final BookingClient bookingClient;


    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader(USER_ID) long userId,
                                                @RequestBody BookingDtoItem bookingDto) {
        if (bookingDto.getItemId() == null) {
            throw new ValidationException("Укажите вещь которую хотите забронировать.");
        }
        return bookingClient.saveBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> bookingApproved(@RequestHeader(USER_ID) long userId,
                                                  @PathVariable long bookingId, @RequestParam boolean approved) {
        return bookingClient.bookingApproved(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findBookingById(@RequestHeader(USER_ID) long userId,
                                                  @PathVariable long bookingId) {
        return bookingClient.findBookingById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> findUsersBookings(@RequestHeader(USER_ID) long userId,
                                                    @RequestParam(defaultValue = "ALL") String state,
                                                    @RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "20") int size) {
        return bookingClient.findUsersBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findOwnersBookings(@RequestHeader(USER_ID) long userId,
                                                     @RequestParam(defaultValue = "ALL") String state,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "20") int size) {
        return bookingClient.findOwnersBookings(userId, state, from, size);
    }
}
