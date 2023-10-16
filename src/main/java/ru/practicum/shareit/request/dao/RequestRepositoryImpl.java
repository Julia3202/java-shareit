package ru.practicum.shareit.request.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.Request;

import java.util.HashMap;
import java.util.Map;

@Repository
public class RequestRepositoryImpl implements RequestRepository {
    private final Map<Long, Request> requestMap = new HashMap<>();

    @Override
    public Request create(Request request) {
        requestMap.put(request.getId(), request);
        return request;
    }

    @Override
    public Request getById(Long requestId) {
        return requestMap.get(requestId);
    }
}
