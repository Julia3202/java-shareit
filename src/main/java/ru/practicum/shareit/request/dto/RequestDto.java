package ru.practicum.shareit.request.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class RequestDto {
    private long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}
