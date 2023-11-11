package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.RequestService;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BookingServiceImplWithPageTest {
    private final EntityManager entityManager;
    private final BookingService bookingService;
    private final ItemService itemService;
    private final UserService userService;
    private final RequestService requestService;
    private final BookingRepository bookingRepository;

    private UserDto userDto;
    private UserDto userDtoTwo;
    private Booking booking;
    private BookingDtoItem bookingDtoItem;
    private LocalDateTime start;
    private LocalDateTime end;

    @BeforeEach
    void beforeEach() {
        start = LocalDateTime.now().plusHours(2);
        end = LocalDateTime.now().plusDays(5);
        userDto = new UserDto(1, "userName", "user@mail.ru");
        userDtoTwo = new UserDto(2, "userTwoName", "userTwo@mail.ru");
        RequestDto requestDto = new RequestDto(1, "requestDescription", null, start.minusDays(2));
        ItemDto itemDto = new ItemDto(1, "itemName", "itemDescription", true, 1L);
        bookingDtoItem = new BookingDtoItem(1, start, end, itemDto.getId(), Status.APPROVED);
        User user = new User(2, "userName", "user@mail.ru");
        Request request = new Request(1, "requestDescription", user, LocalDateTime.now());
        Item item = new Item(1, "itemName", "itemDescription", true, user, request);
        booking = new Booking(1, start, end, item, user, Status.APPROVED);

        userService.create(userDto);
        userService.create(userDtoTwo);
        requestService.createRequest(2, requestDto);
        itemService.create(userDto.getId(), itemDto);
    }

    @Test
    void findUsersAllBookings() {
        bookingService.saveBooking(2, bookingDtoItem);
        List<BookingDto> bookingDtoList = bookingService.findUsersBookings(2, "ALL", 0, 1);
        TypedQuery<Booking> bookingTypedQuery = entityManager.createQuery("select b " +
                "from Booking b " +
                "where b.booker.id = :id " +
                "order by b.start desc ", Booking.class);
        List<Booking> bookingList = bookingTypedQuery.setParameter("id", userDtoTwo.getId()).getResultList();
        Booking booking = bookingList.get(0);
        BookingDto bookingDto = bookingDtoList.get(0);
        assertEquals(bookingDtoList.size(), bookingList.size());
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getStatus(), bookingDto.getStatus());
    }

    @Test
    void findUsersPastBookings() {
        booking.setStart(start.minusDays(10));
        booking.setEnd(start.minusDays(2));
        BookingMapper.toBookingDto(bookingRepository.save(booking));
        List<BookingDto> bookingDtoList = bookingService.findUsersBookings(2, "PAST", 0, 1);
        TypedQuery<Booking> bookingTypedQuery = entityManager.createQuery("select b " +
                "from Booking b " +
                "where b.booker.id = :id and b.end < :time " +
                "order by b.start desc ", Booking.class);
        List<Booking> bookingList = bookingTypedQuery.setParameter("id", userDtoTwo.getId())
                .setParameter("time", start.plusDays(10)).getResultList();
        Booking booking = bookingList.get(0);
        BookingDto bookingDto = bookingDtoList.get(0);
        assertEquals(bookingDtoList.size(), bookingList.size());
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getStatus(), bookingDto.getStatus());
    }

    @Test
    void findUsersCurrentBookings() {
        booking.setStart(start.minusDays(10));
        BookingMapper.toBookingDto(bookingRepository.save(booking));
        List<BookingDto> bookingDtoList = bookingService.findUsersBookings(2, "CURRENT", 0, 1);
        TypedQuery<Booking> bookingTypedQuery = entityManager.createQuery("select b " +
                "from Booking b " +
                "where b.booker.id = :id and b.start < :time and b.end > :time " +
                "order by b.start desc ", Booking.class);
        List<Booking> bookingList = bookingTypedQuery.setParameter("id", userDtoTwo.getId())
                .setParameter("time", start.plusDays(3)).getResultList();
        Booking booking = bookingList.get(0);
        BookingDto bookingDto = bookingDtoList.get(0);
        assertEquals(bookingDtoList.size(), bookingList.size());
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getStatus(), bookingDto.getStatus());
    }

    @Test
    void findUsersFutureBookings() {
        bookingDtoItem.setStart(start.plusDays(2));
        bookingService.saveBooking(2, bookingDtoItem);
        List<BookingDto> bookingDtoList = bookingService.findUsersBookings(2, "FUTURE", 0, 1);
        TypedQuery<Booking> bookingTypedQuery = entityManager.createQuery("select b " +
                "from Booking b " +
                "where b.booker.id = :id and b.start > :time " +
                "order by b.start desc ", Booking.class);
        List<Booking> bookingList = bookingTypedQuery.setParameter("id", userDtoTwo.getId())
                .setParameter("time", LocalDateTime.now()).getResultList();
        Booking booking = bookingList.get(0);
        BookingDto bookingDto = bookingDtoList.get(0);
        assertEquals(bookingDtoList.size(), bookingList.size());
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getStatus(), bookingDto.getStatus());
    }

    @Test
    void findUsersWaitingBookings() {
        bookingDtoItem.setStatus(Status.WAITING);
        bookingService.saveBooking(2, bookingDtoItem);
        List<BookingDto> bookingDtoList = bookingService.findUsersBookings(2, "WAITING", 0, 1);
        TypedQuery<Booking> bookingTypedQuery = entityManager.createQuery("select b " +
                "from Booking b " +
                "where b.booker.id = :id and b.status = 'WAITING' " +
                "order by b.start desc ", Booking.class);
        List<Booking> bookingList = bookingTypedQuery.setParameter("id", userDtoTwo.getId()).getResultList();
        Booking booking = bookingList.get(0);
        BookingDto bookingDto = bookingDtoList.get(0);
        assertEquals(bookingDtoList.size(), bookingList.size());
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getStatus(), bookingDto.getStatus());
    }

    @Test
    void findUsersRejectedBookings() {
        bookingDtoItem.setStatus(Status.REJECTED);
        bookingService.saveBooking(2, bookingDtoItem);
        List<BookingDto> bookingDtoList = bookingService.findUsersBookings(2, "REJECTED", 0, 1);
        TypedQuery<Booking> bookingTypedQuery = entityManager.createQuery("select b " +
                "from Booking b " +
                "where b.booker.id = :id and b.status = 'REJECTED' " +
                "order by b.start desc ", Booking.class);
        List<Booking> bookingList = bookingTypedQuery.setParameter("id", userDtoTwo.getId()).getResultList();
        Booking booking = bookingList.get(0);
        BookingDto bookingDto = bookingDtoList.get(0);
        assertEquals(bookingDtoList.size(), bookingList.size());
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getStatus(), bookingDto.getStatus());
    }

    @Test
    void findUsersBookingsByUnknownState() {
        bookingService.saveBooking(2, bookingDtoItem);
        try {
            bookingService.findUsersBookings(2, "UNSUPPORTED_STATUS", 0, 1);
        } catch (ValidationException exception) {
            assertEquals("Unknown state: UNSUPPORTED_STATUS", exception.getMessage());
        }
    }

    @Test
    void findOwnersBookings() {
        bookingService.saveBooking(2, bookingDtoItem);
        List<BookingDto> bookingDtoList = bookingService.findOwnersBookings(1, "ALL", 0, 1);
        TypedQuery<Booking> bookingTypedQuery = entityManager.createQuery("select b " +
                "from Booking b " +
                "join b.item i " +
                "where i.owner.id = :id " +
                "order by b.start desc ", Booking.class);
        List<Booking> bookingList = bookingTypedQuery.setParameter("id", userDto.getId()).getResultList();
        Booking booking = bookingList.get(0);
        BookingDto bookingDto = bookingDtoList.get(0);
        assertEquals(bookingDtoList.size(), bookingList.size());
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getStatus(), bookingDto.getStatus());
    }

    @Test
    void findOwnersWaitingBookings() {
        bookingDtoItem.setStatus(Status.WAITING);
        bookingService.saveBooking(2, bookingDtoItem);
        List<BookingDto> bookingDtoList = bookingService.findOwnersBookings(1, "WAITING", 0, 1);
        TypedQuery<Booking> bookingTypedQuery = entityManager.createQuery("select b " +
                "from Booking b " +
                "join b.item i " +
                "where i.owner.id = :id and b.status = 'WAITING' " +
                "order by b.start desc ", Booking.class);
        List<Booking> bookingList = bookingTypedQuery.setParameter("id", userDto.getId()).getResultList();
        Booking booking = bookingList.get(0);
        BookingDto bookingDto = bookingDtoList.get(0);
        assertEquals(bookingDtoList.size(), bookingList.size());
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getStatus(), bookingDto.getStatus());
    }

    @Test
    void findOwnersFutureBookings() {
        LocalDateTime newStart = start.plusDays(2);
        bookingDtoItem.setStart(newStart);
        bookingService.saveBooking(2, bookingDtoItem);
        List<BookingDto> bookingDtoList = bookingService.findOwnersBookings(1, "FUTURE", 0, 1);
        TypedQuery<Booking> bookingTypedQuery = entityManager.createQuery("select b " +
                "from Booking b " +
                "join b.item i " +
                "where i.owner.id = :id and b.start > :time  " +
                "order by b.start desc ", Booking.class);
        List<Booking> bookingList = bookingTypedQuery.setParameter("id", userDto.getId())
                .setParameter("time", start).getResultList();
        Booking booking = bookingList.get(0);
        BookingDto bookingDto = bookingDtoList.get(0);
        assertEquals(bookingDtoList.size(), bookingList.size());
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getStatus(), bookingDto.getStatus());
    }

    @Test
    void findOwnersCurrentBookings() {
        booking.setStart(start.minusDays(10));
        BookingMapper.toBookingDto(bookingRepository.save(booking));
        List<BookingDto> bookingDtoList = bookingService.findOwnersBookings(1, "CURRENT", 0, 1);
        TypedQuery<Booking> bookingTypedQuery = entityManager.createQuery("select b " +
                "from Booking b " +
                "join b.item i " +
                "where i.owner.id = :id and b.start < :time and b.end > :time " +
                "order by b.start desc ", Booking.class);
        List<Booking> bookingList = bookingTypedQuery.setParameter("id", userDto.getId())
                .setParameter("time", start.plusDays(3)).getResultList();
        Booking booking = bookingList.get(0);
        BookingDto bookingDto = bookingDtoList.get(0);
        assertEquals(bookingDtoList.size(), bookingList.size());
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getStatus(), bookingDto.getStatus());
    }

    @Test
    void findOwnersPastBookings() {
        booking.setStart(start.minusDays(10));
        booking.setEnd(start.minusDays(2));
        BookingMapper.toBookingDto(bookingRepository.save(booking));
        List<BookingDto> bookingDtoList = bookingService.findOwnersBookings(1, "PAST", 0, 1);
        TypedQuery<Booking> bookingTypedQuery = entityManager.createQuery("select b " +
                "from Booking b " +
                "join b.item i " +
                "where i.owner.id = :id and b.end < :time  " +
                "order by b.start desc ", Booking.class);
        List<Booking> bookingList = bookingTypedQuery.setParameter("id", userDto.getId())
                .setParameter("time", end.plusDays(1)).getResultList();
        Booking booking = bookingList.get(0);
        BookingDto bookingDto = bookingDtoList.get(0);
        assertEquals(bookingDtoList.size(), bookingList.size());
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getStatus(), bookingDto.getStatus());
    }

    @Test
    void findOwnersRejectedBookings() {
        bookingDtoItem.setStatus(Status.REJECTED);
        bookingService.saveBooking(2, bookingDtoItem);
        List<BookingDto> bookingDtoList = bookingService.findOwnersBookings(1, "REJECTED", 0, 1);
        TypedQuery<Booking> bookingTypedQuery = entityManager.createQuery("select b " +
                "from Booking b " +
                "join b.item i " +
                "where i.owner.id = :id and b.status = 'REJECTED' " +
                "order by b.start desc ", Booking.class);
        List<Booking> bookingList = bookingTypedQuery.setParameter("id", userDto.getId()).getResultList();
        Booking booking = bookingList.get(0);
        BookingDto bookingDto = bookingDtoList.get(0);
        assertEquals(bookingDtoList.size(), bookingList.size());
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getStatus(), bookingDto.getStatus());
    }

    @Test
    void findOwnerBookingsByUnknownState() {
        bookingService.saveBooking(2, bookingDtoItem);
        try {
            bookingService.findOwnersBookings(1, "UNSUPPORTED_STATUS", 0, 1);
        } catch (ValidationException exception) {
            assertEquals("Unknown state: UNSUPPORTED_STATUS", exception.getMessage());
        }
    }
}
