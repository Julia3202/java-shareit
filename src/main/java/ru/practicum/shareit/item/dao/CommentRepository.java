package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Comment;

@Component
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
