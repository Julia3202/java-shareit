package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dao.RequestRepository;


@RequiredArgsConstructor
@Service
public class RequestServiseImpl {
    private final RequestRepository requestRepository;
}
