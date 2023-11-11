package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Status;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class BookingDtoItem {
    private long id;
    @NotBlank(message = "Укажите дату начала бронирования.")
    private LocalDateTime start;
    @NotBlank(message = "Укажите дату окончания бронирования.")
    private LocalDateTime end;
    @NotBlank
    private Long itemId;
    private Status status;
}
