package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;


public interface ItemService {

    ItemDto create(long userId, ItemDto itemDto);

    ItemDto update(long userId, ItemDto itemDto, long id);

    ItemDto findItemById(long userId, long itemId);

    List<ItemDto> findAllItemFromUser(long userId);

    List<ItemDto> search(String text);

    void delete(long userId, long id);

    CommentDto saveComment(long userId, long itemId, CommentDto commentDto);
}
