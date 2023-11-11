package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class RequestMapper {
    public static RequestDto toRequestDto(Request request, List<ItemDto> items) {
        return new RequestDto(
                request.getId(),
                request.getDescription(),
                items,
                request.getCreated()
        );
    }

    public static Request toRequest(RequestDto requestDto, User user) {
        return new Request(
                requestDto.getId(),
                requestDto.getDescription(),
                user,
                requestDto.getCreated() != null ? requestDto.getCreated() : LocalDateTime.now()
        );
    }
}
