package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoDated;

import java.util.List;


public interface ItemService {

    ItemDto create(long userId, ItemDto itemDto);

    ItemDto update(long userId, ItemDto itemDto, long id);

    ItemDtoDated findItemById(long userId, long itemId);

    List<ItemDtoDated> findAllItemFromUser(long userId);

    List<ItemDto> search(String text);

    void deleteById(long userId, long id);

    CommentDto saveComment(long userId, long itemId, CommentDto commentDto);
}
