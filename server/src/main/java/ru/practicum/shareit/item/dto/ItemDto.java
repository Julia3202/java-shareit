package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ItemDto {
    private long id;
    @NotBlank(message = "Укажите название.")
    private String name;
    @NotBlank(message = "Укажите описание.")
    private String description;
    @NotBlank(message = "Укажите доступна ли вещь для бронирования.")
    private Boolean available;
    private Long requestId;
}
