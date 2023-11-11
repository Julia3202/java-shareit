package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RequestMapperTest {
    private Request request;
    private RequestDto requestDto;
    private User user;
    List<ItemDto> items = new ArrayList<>();

    @BeforeEach
    void beforeEach() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(5);
        user = new User(1, "userName", "user@mail.ru");
        request = new Request(1, "requestDescription", user, LocalDateTime.now());
        ItemDto itemDto = new ItemDto(1, "itemName", "itemDescription", true, 1L);
        items.add(itemDto);
        requestDto = new RequestDto(1L, "requestDescription", items, end);
    }

    @Test
    void toRequestDto() {
        RequestDto requestDtoTest = RequestMapper.toRequestDto(request, items);
        assertEquals(request.getId(), requestDtoTest.getId());
        assertEquals(request.getDescription(), requestDtoTest.getDescription());
        assertEquals(items, requestDtoTest.getItems());
        assertNotNull(requestDtoTest.getCreated());
    }

    @Test
    void toRequest() {
        Request requestTest = RequestMapper.toRequest(requestDto, user);
        assertEquals(requestDto.getId(), requestTest.getId());
        assertEquals(requestDto.getDescription(), requestTest.getDescription());
        assertEquals(user, requestTest.getRequestor());
        assertNotNull(requestTest.getCreated());
    }
}