package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;

public interface RequestService {
    RequestDto createRequest(long userId, RequestDto requestDto);

    List<RequestDto> findRequestByUserId(long userId);

    List<RequestDto> findAllRequests(long userId, int from, int size);

    RequestDto findRequestById(long userId, long requestId);
}
