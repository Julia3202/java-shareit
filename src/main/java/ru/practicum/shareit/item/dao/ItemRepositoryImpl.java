package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemValidator;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final ItemValidator itemValidator = new ItemValidator();
    private final Map<Long, Item> itemList = new HashMap<>();
    private int count = 1;

    private int generateId() {
        return count++;
    }

    @Override
    public Item create(User user, Item item) {
        itemValidator.validItem(item);
        itemValidator.validAvailable(item);
        long itemId = generateId();
        item.setId(itemId);
        item.setOwner(user);
        itemList.put(itemId, item);
        return item;
    }

    @Override
    public Item update(User user, ItemDto itemDto, long id) {
        Item item = findItem(id);
        if (!item.getOwner().equals(user)) {
            throw new NotFoundException("Пользователь " + user.getName() + " не является владельцем." +
                    " Изменение невозможно.");
        }
        Optional.ofNullable(itemDto.getDescription()).ifPresent(item::setDescription);
        Optional.ofNullable(itemDto.getName()).ifPresent(item::setName);
        Optional.ofNullable(itemDto.getAvailable()).ifPresent(item::setAvailable);
        itemList.put(id, item);
        return item;
    }

    private Item findItem(long id) {
        Item item = itemList.get(id);
        if (item == null) {
            throw new NotFoundException("Вещь с ID- " + id + "не найдена.");
        }
        return item;
    }

    @Override
    public Item findItemById(long userId, long itemId) {
        log.info("Владельцем {} является пользователь с ID - {}.", findItem(itemId).getName(), userId);
        return itemList.get(itemId);
    }

    @Override
    public List<Item> findAllItemFromUser(long userId) {
        List<Item> itemLists = new ArrayList<>();
        for (Item items : itemList.values()) {
            if (items.getOwner().getId() == userId) {
                itemLists.add(items);
            }
        }
        return itemLists;
    }

    @Override
    public List<Item> search(String text) {
        List<Item> itemLists = new ArrayList<>();
        if (text.isBlank()) {
            return itemLists;
        }
        String newText = text.toLowerCase();
        for (Item items : itemList.values()) {
            String description = items.getDescription().toLowerCase();
            String name = items.getName().toLowerCase();
            if (description.contains(newText) || name.contains(newText) || description.startsWith(newText) ||
                    description.endsWith(newText) || name.startsWith(newText) || name.endsWith(newText)) {
                if (items.getAvailable()) {
                    itemLists.add(items);
                }
            }
        }
        return itemLists;
    }

    @Override
    public void removeItem(User user, long id) {
        Item item = findItem(id);
        if (!item.getOwner().equals(user)) {
            throw new NotFoundException("Пользователь " + user.getName() + " не является владельцем." +
                    " Изменение невозможно.");
        }
        itemList.remove(id);
    }
}
