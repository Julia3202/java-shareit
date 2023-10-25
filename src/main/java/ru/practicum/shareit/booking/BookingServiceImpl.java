package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
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

    @Override
    @Transactional
    public BookingDto saveBooking(long userId, BookingDtoItem bookingDtoItem) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID" + userId + " не найден."));
        if (bookingDtoItem.getItemId() == null) {
            throw new ValidationException("Укажите вещь которую хотите забронировать.");
        }
        Item item = itemRepository.findById(bookingDtoItem.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь с ID-" + bookingDtoItem.getItemId() + "не найдена."));
        if (!item.getAvailable()) {
            throw new ValidationException("Данная вещь не доступна для аренды.");
        }
        if (bookingDtoItem.getStart() == null || bookingDtoItem.getEnd() == null) {
            throw new ValidationException("Поля с датой начала и окончания аренды обязательны к заполнению.");
        }
        if (bookingDtoItem.getEnd().isBefore(bookingDtoItem.getStart()) ||
                bookingDtoItem.getEnd().equals(bookingDtoItem.getStart())) {
            throw new ValidationException("Неверно указана дата или время возврата вещи. Дата и время окончания брони " +
                    "должно быть позже даты начала бронирования, поправьте данные и попробуйте повторить ошибку.");
        }
        if (bookingDtoItem.getStart().toString().isBlank() || bookingDtoItem.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Укажите желаемую дату бронирования.");
        }
        if (item.getOwner().equals(user)) {
            throw new NotFoundException("Бронирование невозможно. Вещь принадлежить человеку, отправившему запрос.");
        }
        Booking booking = BookingMapper.toBooking(bookingDtoItem, item, user);
        booking.setStatus(Status.WAITING);
        Booking bookingFromRepository = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(bookingFromRepository);
    }

    @Override
    @Transactional
    public BookingDto bookingApproved(long userId, long bookingId, boolean approved) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID" + userId + " не найден."));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Запрос с ID- " + bookingId + " не найден."));
        if (!booking.getItem().getOwner().equals(user)) {
            throw new NotFoundException("Пользователь с ID" + userId + " не оставлял запрос на вещь с ID -" +
                    booking.getItem().getId());
        }
        if (booking.getStatus() == Status.WAITING) {
            if (approved) {
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
            }
        } else {
            throw new ValidationException("Передан неверный статус.");
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
        if (!(booking.getBooker().equals(user))) {

            if (!(booking.getItem().getOwner().equals(user))) {
                throw new NotFoundException("Невозможно получить данные о бронировании так как пользователь с ID- " +
                        userId + " не является владельцем или автором бронирования.");
            }
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> findUsersBookings(long userId, String state) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID" + userId + " не найден."));
        switch (state.toLowerCase()) {
            case ("all"):
                return bookingRepository.findByBookerIdOrderByStartDesc(userId).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case ("current"):
                return bookingRepository.findAllCurrentByBookerId(userId, LocalDateTime.now()).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case ("past"):
                return bookingRepository.findAllPastByBookerId(userId, LocalDateTime.now()).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case ("future"):
                return bookingRepository.findAllFutureByBookerId(userId, LocalDateTime.now()).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case ("waiting"):
                return bookingRepository.findAllWaitingByBookerId(userId).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case ("rejected"):
                return bookingRepository.findAllRejectedByBookerId(userId).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<BookingDto> findOwnersBookings(long userId, String state) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID" + userId + " не найден."));
        switch (state.toLowerCase()) {
            case ("all"):
                return bookingRepository.findByOwnerIdOrderByStartDesc(userId).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case ("current"):
                return bookingRepository.findAllCurrentByOwnerId(userId, LocalDateTime.now()).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case ("past"):
                return bookingRepository.findAllPastByOwnerId(userId, LocalDateTime.now()).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case ("future"):
                return bookingRepository.findAllFutureByOwnerId(userId, LocalDateTime.now()).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case ("waiting"):
                return bookingRepository.findAllWaitingByOwnerId(userId).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            case ("rejected"):
                return bookingRepository.findAllRejectedByOwnerId(userId).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
