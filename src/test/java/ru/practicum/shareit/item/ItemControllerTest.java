package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoDated;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {
    @MockBean
    private ItemServiceImpl itemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    public static final String USER_ID = "X-Sharer-User-Id";

    private ItemDto itemDto;
    private ItemDtoDated itemDtoDated;
    private CommentDto commentDto;
    private final List<CommentDto> comments = new ArrayList<>();
    private final List<ItemDtoDated> itemDtoDatedList = new ArrayList<>();
    private final List<ItemDto> itemDtoList = new ArrayList<>();

    @BeforeEach
    void beforeEach() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(5);
        itemDto = new ItemDto(1, "itemName", "itemDescription", true, 1L);
        commentDto = new CommentDto(1L, "commentOne", itemDto, "userName", start.plusDays(1));
        comments.add(commentDto);
        BookingDtoForItem lastBooking = new BookingDtoForItem(1L, start, end, 1L, Status.APPROVED);
        BookingDtoForItem nextBooking = new BookingDtoForItem(2L, start.plusDays(2), end, 1L, Status.APPROVED);
        itemDtoDated = new ItemDtoDated(1L, "itemName", "itenDescription", true,
                lastBooking, nextBooking, comments);
        itemDtoDatedList.add(itemDtoDated);
        itemDtoList.add(itemDto);
    }

    @Test
    void create() throws Exception {
        when(itemService.create(anyLong(), any()))
                .thenReturn(itemDto);
        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", notNullValue()))
                .andExpect(jsonPath("$.description", notNullValue()))
                .andExpect(jsonPath("$.available", notNullValue()))
                .andExpect(jsonPath("$.requestId", notNullValue()));
        verify(itemService, times(1))
                .create(anyLong(), any());
    }

    @Test
    void update() throws Exception {
        when(itemService.update(anyLong(), any(), anyLong()))
                .thenReturn(itemDto);
        mockMvc.perform(patch("/items/1")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", notNullValue()))
                .andExpect(jsonPath("$.description", notNullValue()))
                .andExpect(jsonPath("$.available", notNullValue()))
                .andExpect(jsonPath("$.requestId", notNullValue()));
        verify(itemService, times(1))
                .update(anyLong(), any(), anyLong());
    }

    @Test
    void findItemById() throws Exception {
        when(itemService.findItemById(anyLong(), anyLong()))
                .thenReturn(itemDtoDated);
        mockMvc.perform(get("/items/1")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", notNullValue()))
                .andExpect(jsonPath("$.description", notNullValue()))
                .andExpect(jsonPath("$.available", notNullValue()))
                .andExpect(jsonPath("$.lastBooking", notNullValue()))
                .andExpect(jsonPath("$.nextBooking", notNullValue()))
                .andExpect(jsonPath("$.comments", notNullValue()));
        verify(itemService, times(1))
                .findItemById(anyLong(), anyLong());
    }

    @Test
    void findAllItemFromUser() throws Exception {
        when(itemService.findAllItemFromUser(anyLong()))
                .thenReturn(itemDtoDatedList);
        mockMvc.perform((get("/items"))
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDtoDatedList)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].name", notNullValue()))
                .andExpect(jsonPath("$[0].description", notNullValue()))
                .andExpect(jsonPath("$[0].available", notNullValue()))
                .andExpect(jsonPath("$[0].lastBooking", notNullValue()))
                .andExpect(jsonPath("$[0].nextBooking", notNullValue()))
                .andExpect(jsonPath("$[0].comments", hasSize(1)));
        verify(itemService, times(1))
                .findAllItemFromUser(anyLong());
    }

    @Test
    void search() throws Exception {
        when(itemService.search(anyString()))
                .thenReturn(itemDtoList);
        mockMvc.perform(get("/items/search")
                        .header(USER_ID, 1)
                        .param("text", "itemName"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDtoList)));
        verify(itemService, times(1))
                .search(anyString());
    }

    @Test
    void deleteByUserIdAndId() throws Exception {
        mockMvc.perform(delete("/items/1")
                        .header(USER_ID, 1))
                .andExpect(status().isOk());
        verify(itemService, times(1))
                .deleteById(anyLong(), anyLong());
    }

    @Test
    void addComment() throws Exception {
        when(itemService.saveComment(anyLong(), anyLong(), any()))
                .thenReturn(commentDto);
        mockMvc.perform(post("/items/1/comment")
                        .content(objectMapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.text", notNullValue()))
                .andExpect(jsonPath("$.item", notNullValue()))
                .andExpect(jsonPath("$.authorName", notNullValue()))
                .andExpect(jsonPath("$.created", notNullValue()));
        verify(itemService, times(1))
                .saveComment(anyLong(), anyLong(), any());

    }
}