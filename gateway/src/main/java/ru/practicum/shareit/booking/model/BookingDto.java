package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.user.dto.UserDtoBooking;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private long id;
    @NotBlank(message = "Укажите дату начала бронирования.")
    @FutureOrPresent
    private LocalDateTime start;
    @FutureOrPresent
    @NotBlank(message = "Укажите дату окончания бронирования.")
    private LocalDateTime end;
    private ItemDtoBooking item;
    private UserDtoBooking booker;
    private Status status;
}
