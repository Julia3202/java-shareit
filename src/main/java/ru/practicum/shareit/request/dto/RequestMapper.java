package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.Request;

public class RequestMapper {
    public static RequestDto toItemRequestDto(Request request) {
        return new RequestDto(
                request.getId(),
                request.getDescription(),
                request.getRequestor(),
                request.getCreated()
        );
    }

    public static Request toItemRequest(RequestDto requestDto) {
        return new Request(
                requestDto.getId(),
                requestDto.getDescription() != null ? requestDto.getDescription() : null,
                requestDto.getRequestor() != null ? requestDto.getRequestor() : null,
                requestDto.getCreated() != null ? requestDto.getCreated() : null
        );
    }

    public static Request toItemRequest(RequestDto requestDto, Request request) {
        return new Request(
                requestDto.getId(),
                requestDto.getDescription() != null ? requestDto.getDescription() : request.getDescription(),
                requestDto.getRequestor() != null ? requestDto.getRequestor() : request.getRequestor(),
                requestDto.getCreated() != null ? requestDto.getCreated() : request.getCreated()
        );
    }
}
