package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Component
public interface ItemRepository {
    Item create(User user, Item item);

    Item update(User user, ItemDto itemDto, long id);

    Item findItemById(long userId, long itemId);

    List<Item> findAllItemFromUser(long userId);

    List<Item> search(String text);

    void removeItem(User user, long id);
}
