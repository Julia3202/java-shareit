package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class Item {
    private long id;
    private String name;
    @NotBlank
    private String description;
    private Boolean available;
    private User owner;
    private Request request;
}
