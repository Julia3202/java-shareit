package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.DateStatus;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserValidatorService;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final UserValidatorService userValidatorService;
    private final BookingValidator bookingValidator = new BookingValidator();

    @Override
    @Transactional
    public BookingDto saveBooking(long userId, BookingDtoItem bookingDtoItem) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID- " + userId + " не найден."));
        if (bookingDtoItem.getItemId() == null) {
            throw new ValidationException("Укажите вещь которую хотите забронировать.");
        }
        Item item = itemRepository.findById(bookingDtoItem.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь с ID- " + bookingDtoItem.getItemId() + " не найдена."));
        if (!item.getAvailable()) {
            throw new ValidationException("Данная вещь не доступна для аренды.");
        }
        bookingValidator.validUserForBooking(item, user);
        Booking booking = BookingMapper.toBooking(bookingDtoItem, item, user);
        if (bookingDtoItem.getStatus() == null) {
            bookingDtoItem.setStatus(Status.WAITING);
        }
        booking.setStatus(bookingDtoItem.getStatus());
        Booking bookingFromRepository = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(bookingFromRepository);
    }

    @Override
    @Transactional
    public BookingDto bookingApproved(long userId, long bookingId, boolean approved) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID- " + userId + " не найден."));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Запрос с ID- " + bookingId + " не найден."));
        if (!booking.getItem().getOwner().equals(user)) {
            throw new NotFoundException("Пользователь с ID- " + userId + " не оставлял запрос на вещь с ID -" +
                    booking.getItem().getId());
        }
        if (booking.getStatus() == Status.WAITING) {
            if (approved) {
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
            }
        } else {
            throw new ValidationException("Передан неверный статус- " + booking.getStatus() + ".");
        }
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto findBookingById(long userId, long bookingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID- " + userId + " не найден."));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Запрос с ID- " + bookingId + " не найден."));
        if (!(booking.getBooker().equals(user))) {
            if (!(booking.getItem().getOwner().equals(user))) {
                throw new NotFoundException("Невозможно получить данные о бронировании так как пользователь с ID- " +
                        userId + " не является владельцем или автором бронирования.");
            }
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> findUsersBookings(long userId, String state, int from, int size) {
        userValidatorService.existUserById(userId);
        Pageable page = PageRequest.of(from / size, size);
        Page<Booking> bookingPage;
        switch (DateStatus.valueOf(state)) {
            case CURRENT:
                bookingPage = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStart(userId,
                        LocalDateTime.now(), LocalDateTime.now(), page);
                break;
            case PAST:
                bookingPage = bookingRepository.findAllByBookerIdAndEndBeforeAndStatusOrderByStartDesc(userId,
                        LocalDateTime.now(), Status.APPROVED, page);
                break;
            case FUTURE:
                bookingPage = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId,
                        LocalDateTime.now(), page);
                break;
            case WAITING:
                bookingPage = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING, page);
                break;
            case REJECTED:
                bookingPage = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED, page);
                break;
            default:
                bookingPage = bookingRepository.findAllByBookerIdOrderByStartDesc(userId, page);
        }
        return bookingPage.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> findOwnersBookings(long userId, String state, int from, int size) {
        userValidatorService.existUserById(userId);
        Pageable page = PageRequest.of(from / size, size);
        Page<Booking> bookingPage;
        switch (DateStatus.valueOf(state)) {
            case CURRENT:
                bookingPage = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStart(userId,
                        LocalDateTime.now(), LocalDateTime.now(), page);
                break;
            case PAST:
                bookingPage = bookingRepository.findAllByItemOwnerIdAndEndBeforeAndStatusOrderByStartDesc(userId,
                        LocalDateTime.now(), Status.APPROVED, page);
                break;
            case FUTURE:
                bookingPage = bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(userId,
                        LocalDateTime.now(), page);
                break;
            case WAITING:
                bookingPage = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId,
                        Status.WAITING, page);
                break;
            case REJECTED:
                bookingPage = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId,
                        Status.REJECTED, page);
                break;
            default:
                bookingPage = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId, page);
        }
        return bookingPage.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
