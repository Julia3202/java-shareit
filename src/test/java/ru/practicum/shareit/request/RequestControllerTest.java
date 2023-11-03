package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.RequestDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RequestController.class)
@AutoConfigureMockMvc
class RequestControllerTest {
    @MockBean
    private RequestServiceImpl requestService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    public static final String USER_ID = "X-Sharer-User-Id";
    private RequestDto requestDto;
    List<ItemDto> items = new ArrayList<>();
    List<RequestDto> requestDtoList = new ArrayList<>();

    @BeforeEach
    void beforeEach() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(5);
        ItemDto itemDto = new ItemDto(1, "itemName", "itemDescription", true, 1L);
        items.add(itemDto);
        requestDto = new RequestDto(1L, "requestDescription", items, end);
        requestDtoList.add(requestDto);
    }

    @Test
    void createRequest() throws Exception {
        when(requestService.createRequest(anyLong(), any()))
                .thenReturn(requestDto);
        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.description", notNullValue()))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.created", notNullValue()));
        verify(requestService, times(1))
                .createRequest(anyLong(), any());
    }

    @Test
    void findRequestByUserId() throws Exception {
        when(requestService.findRequestByUserId(anyLong()))
                .thenReturn(requestDtoList);
        mockMvc.perform(get("/requests")
                        .content(objectMapper.writeValueAsString(requestDtoList))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].description", notNullValue()))
                .andExpect(jsonPath("$[0].items", hasSize(1)))
                .andExpect(jsonPath("$[0].created", notNullValue()));
        verify(requestService, times(1))
                .findRequestByUserId(anyLong());
    }

    @Test
    void findAllRequests() throws Exception {
        when(requestService.findAllRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(requestDtoList);
        mockMvc.perform(get("/requests/all")
                        .content(objectMapper.writeValueAsString(requestDtoList))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].description", notNullValue()))
                .andExpect(jsonPath("$[0].items", hasSize(1)))
                .andExpect(jsonPath("$[0].created", notNullValue()));
        verify(requestService, times(1))
                .findAllRequests(anyLong(), anyInt(), anyInt());
    }

    @Test
    void findRequestById() throws Exception {
        when(requestService.findRequestById(anyLong(), anyLong()))
                .thenReturn(requestDto);
        mockMvc.perform(get("/requests/1")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.description", notNullValue()))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.created", notNullValue()));
        verify(requestService, times(1))
                .findRequestById(anyLong(), anyLong());
    }
}