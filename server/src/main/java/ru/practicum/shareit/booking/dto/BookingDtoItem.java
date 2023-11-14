package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class BookingDtoItem {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private Status status;
}
