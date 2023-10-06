package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Override
    public ItemDto create(long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId);
        Request request = null;
        if (itemDto.getRequestId() != null) {
            request = requestRepository.getById(itemDto.getRequestId());
        }
        Item item = ItemMapper.toItem(itemDto, user, request);
        Item itemRepo = itemRepository.create(user, item);
        return ItemMapper.toItemDto(itemRepo);
    }

    @Override
    public ItemDto update(long userId, ItemDto itemDto, long id) {
        User user = userRepository.findById(userId);
        Item item = itemRepository.update(user, itemDto, id);
        Request request = null;
        Item itemFromDto = ItemMapper.toItem(itemDto, item, request);
        itemFromDto.setId(id);
        return ItemMapper.toItemDto(itemFromDto);
    }

    @Override
    public ItemDto findItemById(long userId, long itemId) {
        Item item = itemRepository.findItemById(userId, itemId);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> findAllItemFromUser(long userId) {
        List<Item> itemList = itemRepository.findAllItemFromUser(userId);
        return itemList.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        List<Item> itemList = itemRepository.search(text);
        return itemList.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public void removeItem(long userId, long id) {
        User user = userRepository.findById(userId);
        itemRepository.removeItem(user, id);
    }
}
