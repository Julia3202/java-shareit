package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CommentMapperTest {

    private Item item;
    private User user;
    private Request request;
    private ItemDto itemDto;
    private CommentDto commentDto;
    private Comment comment;

    @BeforeEach
    void BeforeEach() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(5);
        item = new Item(1, "itemName", "itemDescription", true, user, request);
        itemDto = new ItemDto(1, "itemName", "itemDescription", true, 1L);
        commentDto = new CommentDto(1L, "commentOne", itemDto, "userName", start.plusDays(1));
        user = new User(1, "userName", "user@mail.ru");
        comment = new Comment(1L, "commentOne", item, user, start.plusDays(8));
        request = new Request(1, "requestDescription", user, LocalDateTime.now());
    }

    @Test
    void toCommentDto() {
        CommentDto commentDtoTest = CommentMapper.toCommentDto(comment);
        assertEquals(comment.getId(), commentDtoTest.getId());
        assertEquals(comment.getText(), commentDtoTest.getText());
        assertEquals(itemDto.getId(), commentDtoTest.getItem().getId());
        assertEquals(comment.getAuthor().getName(), commentDtoTest.getAuthorName());
        assertEquals(comment.getCreated(), commentDtoTest.getCreated());
    }

    @Test
    void toComment() {
        Comment commentTest = CommentMapper.toComment(commentDto, item, user);
        assertEquals(commentDto.getId(), commentTest.getId());
        assertEquals(commentDto.getText(), commentTest.getText());
        assertEquals(item, commentTest.getItem());
        assertEquals(user, commentTest.getAuthor());
        assertNotNull(commentTest.getCreated());
    }
}