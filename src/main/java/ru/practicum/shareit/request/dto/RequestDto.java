package ru.practicum.shareit.request.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class RequestDto {
    private long id;
    @NotBlank
    private String description;
    private List<ItemDto> items;
    private LocalDateTime created;
}
