package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.model.RequestDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
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
    private RequestClient requestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    private static final String USER_ID = "X-Sharer-User-Id";
    private RequestDto requestDto;
    private ResponseEntity<Object> responseEntity;

    @BeforeEach
    void beforeEach() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(5);
        requestDto = new RequestDto(1L, "requestDescription", null, end);
        responseEntity = new ResponseEntity<>(requestDto, HttpStatus.OK);
    }

    @Test
    void createRequest() throws Exception {
        when(requestClient.createRequest(anyLong(), any()))
                .thenReturn(responseEntity);
        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.description", notNullValue()))
                .andExpect(jsonPath("$.created", notNullValue()));
        verify(requestClient, times(1))
                .createRequest(anyLong(), any());
    }

    @Test
    void findRequestByUserId() throws Exception {
        ResponseEntity<Object> responseWithList = new ResponseEntity<>(List.of(requestDto), HttpStatus.OK);
        when(requestClient.findRequestByUserId(anyLong()))
                .thenReturn(responseWithList);
        mockMvc.perform(get("/requests")
                        .content(objectMapper.writeValueAsString(List.of(requestDto)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].description", notNullValue()))
                .andExpect(jsonPath("$[0].created", notNullValue()));
        verify(requestClient, times(1))
                .findRequestByUserId(anyLong());
    }

    @Test
    void findAllRequests() throws Exception {
        ResponseEntity<Object> responseWithList = new ResponseEntity<>(List.of(requestDto), HttpStatus.OK);
        when(requestClient.findAllRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(responseWithList);
        mockMvc.perform(get("/requests/all")
                        .content(objectMapper.writeValueAsString(List.of(requestDto)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].description", notNullValue()))
                .andExpect(jsonPath("$[0].created", notNullValue()));
        verify(requestClient, times(1))
                .findAllRequests(anyLong(), anyInt(), anyInt());
    }

    @Test
    void findRequestById() throws Exception {
        when(requestClient.findRequestById(anyLong(), anyLong()))
                .thenReturn(responseEntity);
        mockMvc.perform(get("/requests/1")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.description", notNullValue()))
                .andExpect(jsonPath("$.created", notNullValue()));
        verify(requestClient, times(1))
                .findRequestById(anyLong(), anyLong());
    }
}