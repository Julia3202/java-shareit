package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemMapperTest {
    private Item item;
    private User user;
    private Request request;
    private ItemDto itemDto;
    private final List<CommentDto> comments = new ArrayList<>();
    private BookingDtoForItem lastBooking;
    private BookingDtoForItem nextBooking;

    @BeforeEach
    void beforeEach() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(5);
        itemDto = new ItemDto(1, "itemName", "itemDescription", true, 1L);
        CommentDto commentDto = new CommentDto(1L, "commentOne", itemDto, "userName", start.plusDays(1));
        comments.add(commentDto);
        user = new User(1, "userName", "user@mail.ru");
        request = new Request(1, "requestDescription", user, LocalDateTime.now());
        item = new Item(1, "itemName", "itemDescription", true, user, request);
        lastBooking = new BookingDtoForItem(1L, start, end, 1L, Status.APPROVED);
        nextBooking = new BookingDtoForItem(2L, start.plusDays(2), end, 1L, Status.APPROVED);
    }

    @Test
    void toItemDto() {
        ItemDto itemDto = ItemMapper.toItemDto(item);
        assertEquals(item.getId(), itemDto.getId());
        assertEquals(item.getName(), itemDto.getName());
        assertEquals(item.getDescription(), itemDto.getDescription());
        assertEquals(item.getAvailable(), itemDto.getAvailable());
        assertEquals(item.getRequest().getId(), itemDto.getRequestId());
    }

    @Test
    void toItemDtoDated() {
        ItemDtoDated itemDtoDated = ItemMapper.toItemDtoDated(item, lastBooking, nextBooking, comments);
        assertEquals(item.getId(), itemDtoDated.getId());
        assertEquals(item.getName(), itemDtoDated.getName());
        assertEquals(item.getDescription(), itemDtoDated.getDescription());
        assertEquals(item.getAvailable(), itemDtoDated.getAvailable());
        assertEquals(lastBooking, itemDtoDated.getLastBooking());
        assertEquals(nextBooking, itemDtoDated.getNextBooking());
        assertEquals(1, comments.size());
        CommentDto commentDtoTest = new CommentDto(1L, "commentDtoName", itemDto, "authorName",
                LocalDateTime.now().plusDays(5));
        comments.add(commentDtoTest);
        assertEquals(2, comments.size());
    }

    @Test
    void toItemDtoBooking() {
        ItemDtoBooking itemDtoBooking = ItemMapper.toItemDtoBooking(item);
        assertEquals(item.getId(), itemDtoBooking.getId());
        assertEquals(item.getName(), itemDtoBooking.getName());
    }

    @Test
    void toItem() {
        Item itemTest = ItemMapper.toItem(itemDto, user, request);
        assertEquals(itemDto.getId(), itemTest.getId());
        assertEquals(itemDto.getName(), itemTest.getName());
        assertEquals(itemDto.getDescription(), itemTest.getDescription());
        assertEquals(itemDto.getAvailable(), itemTest.getAvailable());
        assertEquals(user, itemTest.getOwner());
        assertEquals(request, itemTest.getRequest());
    }

    @Test
    void testToItem() {
        Item itemTest = ItemMapper.toItem(itemDto, item, request);
        assertEquals(itemDto.getId(), itemTest.getId());
        assertEquals(itemDto.getName(), itemTest.getName());
        assertEquals(itemDto.getDescription(), itemTest.getDescription());
        assertEquals(itemDto.getAvailable(), itemTest.getAvailable());
        assertEquals(item.getOwner(), itemTest.getOwner());
        assertEquals(request, itemTest.getRequest());
    }
}