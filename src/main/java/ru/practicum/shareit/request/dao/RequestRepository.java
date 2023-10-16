package ru.practicum.shareit.request.dao;

import ru.practicum.shareit.request.model.Request;

public interface RequestRepository {
    Request create(Request request);

    Request getById(Long requestId);
}
