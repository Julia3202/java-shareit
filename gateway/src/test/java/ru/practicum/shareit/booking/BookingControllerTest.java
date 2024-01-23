package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.model.BookingDtoItem;
import ru.practicum.shareit.booking.model.Status;

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

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {
    @MockBean
    private BookingClient bookingClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private static final String USER_ID = "X-Sharer-User-Id";

    private BookingDtoItem bookingDto;
    private ResponseEntity<Object> responseEntity;
    private final List<BookingDtoItem> bookingDtoList = new ArrayList<>();

    @BeforeEach
    void beforeEach() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(5);
        bookingDto = new BookingDtoItem(1, start, end, 1L, Status.APPROVED);
        bookingDtoList.add(bookingDto);
        responseEntity = new ResponseEntity<>(bookingDto, HttpStatus.OK);
    }

    @Test
    void createBooking() throws Exception {
        when(bookingClient.saveBooking(anyLong(), any()))
                .thenReturn(responseEntity);
        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", notNullValue()))
                .andExpect(jsonPath("$.end", notNullValue()))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
        verify(bookingClient, times(1))
                .saveBooking(anyLong(), any());
    }

    @Test
    void bookingApproved() throws Exception {
        when(bookingClient.bookingApproved(1, 1, true))
                .thenReturn(responseEntity);
        mockMvc.perform(patch("/bookings/1")
                        .header(USER_ID, 1)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", notNullValue()))
                .andExpect(jsonPath("$.end", notNullValue()))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
        verify(bookingClient, times(1))
                .bookingApproved(1, 1, true);
    }

    @Test
    void findBookingById() throws Exception {
        when(bookingClient.findBookingById(1, 1L))
                .thenReturn(responseEntity);
        mockMvc.perform(get("/bookings/1")
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", notNullValue()))
                .andExpect(jsonPath("$.end", notNullValue()))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
        verify(bookingClient, times(1))
                .findBookingById(1, 1L);
    }

    @Test
    void findUsersBookings() throws Exception {
        ResponseEntity<Object> responseWithList = new ResponseEntity<>(bookingDtoList, HttpStatus.OK);
        when(bookingClient.findUsersBookings(1, "PAST", 0, 1))
                .thenReturn(responseWithList);
        mockMvc.perform(get("/bookings")
                        .header(USER_ID, 1)
                        .param("state", "PAST")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDtoList)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", notNullValue()))
                .andExpect(jsonPath("$[0].end", notNullValue()))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus().toString())));
        verify(bookingClient, times(1))
                .findUsersBookings(1, "PAST", 0, 1);
    }

    @Test
    void findOwnersBookings() throws Exception {
        ResponseEntity<Object> responseWithList = new ResponseEntity<>(bookingDtoList, HttpStatus.OK);
        when(bookingClient.findOwnersBookings(1, "PAST", 0, 1))
                .thenReturn(responseWithList);
        mockMvc.perform(get("/bookings/owner")
                        .header(USER_ID, 1)
                        .param("state", "PAST")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDtoList)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", notNullValue()))
                .andExpect(jsonPath("$[0].end", notNullValue()))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus().toString())));
        verify(bookingClient, times(1))
                .findOwnersBookings(1, "PAST", 0, 1);
    }
}