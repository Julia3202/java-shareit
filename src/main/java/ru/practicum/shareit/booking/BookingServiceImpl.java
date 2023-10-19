package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.OtherException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingDto saveBooking(long userId, BookingDto bookingDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID" + userId + " не найден."));
        Item item = itemRepository.findById(bookingDto.getItem().getId())
                .orElseThrow(() -> new NotFoundException("Вещь с ID-" + bookingDto.getItem().getId() + "не найдена."));
        if (!item.getAvailable()) {
            throw new OtherException("Данная вещь не доступна для аренды.");
        }
        if (item.getOwner().equals(user)) {
            throw new OtherException("Невозможно забронировать. Вы являетесь владельцем вещи.");
        }
        Booking booking = BookingMapper.toBooking(bookingDto, item, user);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto bookingApproved(long userId, long bookingId, boolean approved) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID" + userId + " не найден."));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Запрос с ID- " + bookingId + " не найден."));
        if (booking.getStatus() == Status.WAITING) {
            if (approved) {
                booking.setStatus(Status.APPROVED);
            }
            booking.setStatus(Status.REJECTED);
        }
        Booking saveBooking = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(saveBooking);
    }

    @Override
    public BookingDto findBookingById(long userId, long bookingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID" + userId + " не найден."));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Запрос с ID- " + bookingId + " не найден."));
        if (!booking.getBooker().equals(user) || !booking.getItem().getOwner().equals(user)) {
            throw new NotFoundException("Невозможно получить данные о бронировании так как пользователь с ID- " +
                    userId + " не является владельцем или автором бронирования.");
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> findUsersBookings(long userId, String state) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID" + userId + " не найден."));
        List<Booking> bookingList = bookingRepository.findByBookerId(userId);
        return bookingList.stream()
                .map(BookingMapper::toBookingDto)
                .sorted(Comparator.comparing(BookingDto::getStart).reversed())
                .collect(Collectors.toList());

    }

    @Override
    public List<BookingDto> findOwnersBookings(long userId, String state) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID" + userId + " не найден."));
        List<Booking> bookingList = bookingRepository.findByItemOwnerId(userId);
        return bookingList.stream()
                .map(BookingMapper::toBookingDto)
                .sorted(Comparator.comparing(BookingDto::getStart).reversed())
                .collect(Collectors.toList());
    }
}
