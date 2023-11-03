package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.UserValidatorService;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDtoBooking;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    private BookingRepository bookingRepository;
    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private BookingServiceImpl bookingService;
    private Item item;
    private User user;
    private User userTwo;
    private Booking booking;
    private BookingDtoItem bookingDtoItem;

    @BeforeEach
    void beforeEach() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusDays(5);
        user = new User(1, "userName", "user@mail.ru");
        userTwo = new User(2, "user2Name", "user2@mail,ru");
        Request request = new Request(1, "requestDescription", user, start.plusHours(2));
        item = new Item(1, "itemName", "itemDescription", true, userTwo, request);
        booking = new Booking(1, start, end, item, user, Status.APPROVED);
        bookingDtoItem = new BookingDtoItem(1, start, end, item.getId(), Status.APPROVED);
        ItemDtoBooking itemDtoBooking = new ItemDtoBooking(1, "itemName");
        ItemDtoBooking itemDtoBookingTwo = new ItemDtoBooking(2, "itemTwoName");
        Item itemTwo = new Item(2, "itemTwoName", "itemTwoDescription", true, userTwo, null);
        UserDtoBooking userDtoBooking = new UserDtoBooking(1);
        BookingDto bookingDto = new BookingDto(1, start, end, itemDtoBooking, userDtoBooking, Status.APPROVED);
        BookingDto bookingDtoTwo = new BookingDto(2, start.plusHours(9), end, itemDtoBookingTwo, userDtoBooking, Status.APPROVED);
        BookingDtoItem bookingDtoItemTwo = new BookingDtoItem(2, start.plusHours(9), end, 2L, Status.APPROVED);
        userRepository = mock(UserRepository.class);
        UserValidatorService userValidatorService = new UserValidatorService(userRepository);
        bookingRepository = mock(BookingRepository.class);
        itemRepository = mock(ItemRepository.class);
        bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository, userValidatorService);
    }

    @Test
    void saveBooking() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        when(bookingRepository.save(any()))
                .thenReturn(booking);
        BookingDto bookingDto = bookingService.saveBooking(1, bookingDtoItem);
        assertEquals(bookingDto.getId(), bookingDtoItem.getId());
        assertEquals(bookingDto.getItem().getId(), bookingDtoItem.getItemId());
        assertEquals(bookingDto.getEnd(), bookingDtoItem.getEnd());
        assertEquals(bookingDto.getStart(), bookingDtoItem.getStart());
        assertEquals(bookingDto.getStatus(), bookingDtoItem.getStatus());
        verify(userRepository, times(1))
                .findById(anyLong());
        verify(itemRepository, times(1))
                .findById(anyLong());
        verify(bookingRepository, times(1))
                .save(any());

    }

    @Test
    void bookingApproved() {
        booking.setStatus(Status.WAITING);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userTwo));
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        when(bookingRepository.save(any()))
                .thenReturn(booking);
        BookingDto bookingDto = bookingService.bookingApproved(1, 1, true);
        assertEquals(Status.APPROVED, bookingDto.getStatus());
        verify(userRepository, times(1))
                .findById(anyLong());
        verify(bookingRepository, times(1))
                .findById(anyLong());
        verify(bookingRepository, times(1))
                .save(any());
    }

    @Test
    void findBookingById() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userTwo));
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        BookingDto bookingDto = bookingService.findBookingById(1, 1);
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getStatus(), bookingDto.getStatus());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        verify(userRepository, times(1))
                .findById(anyLong());
        verify(bookingRepository, times(1))
                .findById(anyLong());
    }

    @Test
    void findUsersBookings() {

    }

    @Test
    void findOwnersBookings() {
    }
}