package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.model.CommentDto;
import ru.practicum.shareit.item.model.ItemDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
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
    private ItemClient itemClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    public static final String USER_ID = "X-Sharer-User-Id";

    private ItemDto itemDto;
    private CommentDto commentDto;
    private ResponseEntity<Object> itemResponse;
    private ResponseEntity<Object> commentResponse;

    @BeforeEach
    void beforeEach() {
        LocalDateTime start = LocalDateTime.now();
        itemDto = new ItemDto(1, "itemName", "itemDescription", true, 1L);
        commentDto = new CommentDto(1L, "commentOne", itemDto, "userName", start.plusDays(1));
        itemResponse = new ResponseEntity<>(itemDto, HttpStatus.OK);
        commentResponse = new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    @Test
    void create() throws Exception {
        when(itemClient.create(anyLong(), any()))
                .thenReturn(itemResponse);
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
        verify(itemClient, times(1))
                .create(anyLong(), any());
    }

    @Test
    void update() throws Exception {
        when(itemClient.update(anyLong(), any(), anyLong()))
                .thenReturn(itemResponse);
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
        verify(itemClient, times(1))
                .update(anyLong(), any(), anyLong());
    }

    @Test
    void findItemById() throws Exception {
        when(itemClient.findItemById(anyLong(), anyLong()))
                .thenReturn(itemResponse);
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
                .andExpect(jsonPath("$.available", notNullValue()));
        verify(itemClient, times(1))
                .findItemById(anyLong(), anyLong());
    }

    @Test
    void findAllItemFromUser() throws Exception {
        ResponseEntity<Object> responseWithList = new ResponseEntity<>(List.of(itemDto), HttpStatus.OK);
        when(itemClient.findAllItemFromUser(anyLong()))
                .thenReturn(responseWithList);
        mockMvc.perform((get("/items"))
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].name", notNullValue()))
                .andExpect(jsonPath("$[0].description", notNullValue()))
                .andExpect(jsonPath("$[0].available", notNullValue()));
        verify(itemClient, times(1))
                .findAllItemFromUser(anyLong());
    }

    @Test
    void search() throws Exception {
        ResponseEntity<Object> responseWithList = new ResponseEntity<>(List.of(itemDto), HttpStatus.OK);
        when(itemClient.search(anyString()))
                .thenReturn(responseWithList);
        mockMvc.perform(get("/items/search")
                        .header(USER_ID, 1)
                        .param("text", "itemName"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(itemDto))));
        verify(itemClient, times(1))
                .search(anyString());
    }

    @Test
    void deleteByUserIdAndId() throws Exception {
        mockMvc.perform(delete("/items/1")
                        .header(USER_ID, 1))
                .andExpect(status().isOk());
        verify(itemClient, times(1))
                .deleteById(anyLong(), anyLong());
    }

    @Test
    void addComment() throws Exception {
        when(itemClient.saveComment(anyLong(), anyLong(), any()))
                .thenReturn(commentResponse);
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
        verify(itemClient, times(1))
                .saveComment(anyLong(), anyLong(), any());

    }
}