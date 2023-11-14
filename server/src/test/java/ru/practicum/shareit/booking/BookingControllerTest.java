package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.user.dto.UserDtoBooking;

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
    private BookingServiceImpl bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private static final String USER_ID = "X-Sharer-User-Id";

    private BookingDto bookingDto;
    private BookingDtoItem bookingDtoItem;
    private final List<BookingDto> bookingDtoList = new ArrayList<>();

    @BeforeEach
     void beforeEach() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(5);
        ItemDtoBooking itemDto = new ItemDtoBooking(1, "nameItem");
        UserDtoBooking userDto = new UserDtoBooking(1);
        bookingDto = new BookingDto(1, start, end, itemDto, userDto, Status.APPROVED);
        bookingDtoItem = new BookingDtoItem(1, start, end, itemDto.getId(), Status.APPROVED);
        bookingDtoList.add(bookingDto);
    }

    @Test
    void createBooking() throws Exception {
        when(bookingService.saveBooking(anyLong(), any()))
                .thenReturn(bookingDto);
        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingDtoItem))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", notNullValue()))
                .andExpect(jsonPath("$.end", notNullValue()))
                .andExpect(jsonPath("$.item", notNullValue()))
                .andExpect(jsonPath("$.booker", notNullValue()))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
        verify(bookingService, times(1))
                .saveBooking(anyLong(), any());
    }

    @Test
    void bookingApproved() throws Exception {
        when(bookingService.bookingApproved(1, 1, true))
                .thenReturn(bookingDto);
        mockMvc.perform(patch("/bookings/1")
                        .header(USER_ID, 1)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", notNullValue()))
                .andExpect(jsonPath("$.end", notNullValue()))
                .andExpect(jsonPath("$.item", notNullValue()))
                .andExpect(jsonPath("$.booker", notNullValue()))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
        verify(bookingService, times(1))
                .bookingApproved(1, 1, true);
    }

    @Test
    void findBookingById() throws Exception {
        when(bookingService.findBookingById(1, 1))
                .thenReturn(bookingDto);
        mockMvc.perform(get("/bookings/1")
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", notNullValue()))
                .andExpect(jsonPath("$.end", notNullValue()))
                .andExpect(jsonPath("$.item", notNullValue()))
                .andExpect(jsonPath("$.booker", notNullValue()))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
        verify(bookingService, times(1))
                .findBookingById(1, 1);
    }

    @Test
    void findUsersBookings() throws Exception {
        when(bookingService.findUsersBookings(1, "PAST", 0, 1))
                .thenReturn(bookingDtoList);
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
        verify(bookingService, times(1))
                .findUsersBookings(1, "PAST", 0, 1);
    }

    @Test
    void findOwnersBookings() throws Exception {
        when(bookingService.findOwnersBookings(1, "PAST", 0, 1))
                .thenReturn(bookingDtoList);
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
        verify(bookingService, times(1))
                .findOwnersBookings(1, "PAST", 0, 1);
    }
}