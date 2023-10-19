package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.OtherException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemValidator itemValidator = new ItemValidator();

    @Override
    @Transactional
    public ItemDto create(long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID" + userId + " не найден."));
        Request request = null;
        if (itemDto.getRequestId() != null) {
            request = requestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Запрос с ID - " + itemDto.getRequestId() + " не найден."));
        }
        Item item = ItemMapper.toItem(itemDto, user, request);
        itemValidator.validItem(item);
        Item itemRepo = itemRepository.save(item);
        return ItemMapper.toItemDto(itemRepo);
    }

    @Override
    @Transactional
    public ItemDto update(long userId, ItemDto itemDto, long id) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID" + id + " не найден."));
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещь с ID- " + id + "не найдена."));
        if (!item.getOwner().equals(user)) {
            throw new NotFoundException("Пользователь " + user.getName() + " не является владельцем." +
                    " Изменение невозможно.");
        }
        Request request = null;
        if (itemDto.getRequestId() != null) {
            request = requestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Запрос с ID - " + itemDto.getRequestId() + " не найден."));
        }
        Item itemFromDto = ItemMapper.toItem(itemDto, item, request);
        itemFromDto.setId(id);
        return ItemMapper.toItemDto(itemRepository.save(itemFromDto));
    }

    @Override
    public ItemDto findItemById(long userId, long itemId) {
        Item item = itemRepository.findItemById(itemId);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> findAllItemFromUser(long userId) {
        List<Item> itemList = itemRepository.findByOwner_Id(userId);
        return itemList.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        List<Item> itemList = itemRepository.findByNameOrDescription(text);
        return itemList.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void delete(long userId, long id) {
        itemRepository.deleteByOwner_IdAndId(userId, id);
    }

    @Override
    @Transactional
    public CommentDto saveComment(long userId, long itemId, CommentDto commentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID" + userId + " не найден."));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с ID- " + itemId + "не найдена."));
        List<Booking> bookingList = bookingRepository.findAllByItemIdAndBookerIdAndStatusAndStartBeforeAndEndBefore(
                itemId, userId, Status.APPROVED, LocalDateTime.now(), LocalDateTime.now());
        if (bookingList.isEmpty()) {
            throw new OtherException("Пользователь с ID- " + userId + " не может писать отзыв на вещь с ID- " + itemId +
                    ", т.к. не соблюдены условия. Отзыв может\n" +
                    "оставить только тот пользователь, который брал эту вещь в аренду, и только после\n" +
                    "окончания срока аренды.");
        }
        Comment comment = CommentMapper.toComment(commentDto, item, user);
        Comment commentFromRepository = commentRepository.save(comment);
        return CommentMapper.toCommentDto(commentFromRepository);
    }
}
