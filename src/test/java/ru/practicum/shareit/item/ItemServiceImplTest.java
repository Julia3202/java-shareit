package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoDated;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ItemServiceImplTest {

    private final EntityManager entityManager;
    private final ItemService itemService;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private ItemDto itemDto;
    private ItemDtoDated itemDtoDated;
    private CommentDto commentDto;
    private UserDto userDto;
    private UserDto userComment;
    private LocalDateTime start;

    @BeforeEach
    void beforeEach() {
        start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(5);
        userDto = new UserDto(1, "userName", "user@mail.ru");
        itemDto = new ItemDto(1, "itemName", "itemDescription", true, null);
        BookingDtoForItem lastBooking = new BookingDtoForItem(1L, start, end, 1L, Status.APPROVED);
        BookingDtoForItem nextBooking = new BookingDtoForItem(2L, start.plusDays(2), end, 1L, Status.APPROVED);
        commentDto = new CommentDto(1L, "textComment", itemDto, "user2Name", start);
        List<CommentDto> comments = List.of(commentDto);
        itemDtoDated = new ItemDtoDated(1L, "itemName", "itemDescription",
                true, lastBooking, nextBooking, comments);
        userComment = new UserDto(2, "user2Name", "user2@mail.ru");
        userService.create(userDto);
        userService.create(userComment);
    }

    @Test
    void create() {
        itemService.create(1, itemDto);
        TypedQuery<Item> query = entityManager.createQuery("select i from Item i where i.id = :id", Item.class);
        Item item = query.setParameter("id", userDto.getId()).getSingleResult();
        assertEquals(item.getId(), itemDto.getId());
        assertEquals(item.getName(), itemDto.getName());
        assertEquals(item.getDescription(), itemDto.getDescription());
    }

    @Test
    void update() {
        itemService.create(1, itemDto);
        itemDto.setName("itemsName");
        itemService.update(userDto.getId(), itemDto, itemDto.getId());
        TypedQuery<Item> query = entityManager.createQuery("select i from Item i where i.id = :id", Item.class);
        Item item = query.setParameter("id", userDto.getId()).getSingleResult();
        assertEquals("itemsName", item.getName());
    }

    @Test
    void findItemById() {
        itemService.create(1, itemDto);
        itemDtoDated = itemService.findItemById(userDto.getId(), itemDto.getId());
        assertEquals(itemDtoDated.getName(), itemDto.getName());
        assertEquals(itemDtoDated.getDescription(), itemDto.getDescription());
    }

    @Test
    void findAllItemFromUser() {
        itemService.create(1, itemDto);
        List<ItemDtoDated> dtoDatedList = itemService.findAllItemFromUser(userDto.getId());
        assertEquals(1, dtoDatedList.size());
    }

    @Test
    void search() {
        itemService.create(1, itemDto);
        String text = "";
        List<ItemDto> itemDtoList = itemService.search(text);
        assertEquals(0, itemDtoList.size());
        text = "iTeM";
        itemDtoList = itemService.search(text);
        assertEquals(itemDto.getName(), itemDtoList.get(0).getName());
    }

    @Test
    void deleteById() {
        itemService.create(1, itemDto);
        ItemDtoDated item = itemService.findItemById(1, 1);
        assertEquals(itemDto.getId(), item.getId());
        itemService.deleteById(1, 1);
        try {
            itemDtoDated = itemService.findItemById(userDto.getId(), itemDto.getId());
        } catch (NotFoundException exception) {
            assertEquals("Вещь с ID- " + itemDto.getId() + " не найдена.", exception.getMessage());
        }
    }

    @Test
    void saveComment() {
        ItemDto itemDtoTest = new ItemDto(2, "itemTwoName", "itemTwoDescription", true, null);
        Item item = ItemMapper.toItem(itemDtoTest, UserMapper.toUser(userDto), null);
        Booking booking = new Booking(1, start.minusDays(20), start.minusDays(15), item, UserMapper.toUser(userComment), Status.APPROVED);
        itemService.create(1, itemDto);
        itemService.create(1, itemDtoTest);
        BookingMapper.toBookingDto(bookingRepository.save(booking));
        CommentDto commentDtoTest = new CommentDto(1L, "textComment", itemDtoTest, userComment.getName(), start.plusDays(15));
        CommentDto comment = itemService.saveComment(userComment.getId(), 2, commentDtoTest);
        assertEquals(commentDto.getText(), comment.getText());
    }
}