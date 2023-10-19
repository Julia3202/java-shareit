package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dao.RequestRepository;


@AllArgsConstructor
@NoArgsConstructor(force = true)
@Service
public class RequestServiseImpl {
    private final RequestRepository requestRepository;
}
