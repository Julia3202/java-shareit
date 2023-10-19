package ru.practicum.shareit.request.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.model.Request;
@Component
public interface RequestRepository extends JpaRepository<Request, Long> {
}
