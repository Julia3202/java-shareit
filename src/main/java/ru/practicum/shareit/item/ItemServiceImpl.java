package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.UserValidatorService;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserValidatorService userValidatorService;
    private final ItemValidator itemValidator = new ItemValidator();

    @Override
    @Transactional
    public ItemDto create(long userId, ItemDto itemDto) {
        itemValidator.validItem(itemDto);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID" + userId + " не найден."));
        Request request = null;
        if (itemDto.getRequestId() != null) {
            request = requestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Запрос с ID - " + itemDto.getRequestId() + " не найден."));
        }
        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(itemDto, user, request)));
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
        Optional.ofNullable(itemDto.getDescription()).ifPresent(item::setDescription);
        Optional.ofNullable(itemDto.getName()).ifPresent(item::setName);
        Optional.ofNullable(itemDto.getAvailable()).ifPresent(item::setAvailable);
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
    public ItemDtoDated findItemById(long userId, long itemId) {
        userValidatorService.byExistUser(userId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с ID- " + itemId + " не найдена."));
        List<CommentDto> commentDtoList = commentRepository.findCommentsByItemId(itemId).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
        if (item.getOwner().getId() != userId) {
            return ItemMapper.toItemDtoDated(item, null, null, commentDtoList);
        }
        List<Booking> lastBooking = bookingRepository.findLastBookingByItemId(itemId, LocalDateTime.now());
        BookingDtoForItem lastBookingFinal = BookingMapper.toItemDtoBooking(lastBooking.isEmpty()
                ? null : lastBooking.get(0));
        List<Booking> nextBooking = bookingRepository.findNextBookingByItemId(itemId, LocalDateTime.now());
        BookingDtoForItem nextBookingFinal = BookingMapper.toItemDtoBooking(nextBooking.isEmpty()
                ? null : nextBooking.get(0));
        return ItemMapper.toItemDtoDated(item, lastBookingFinal, nextBookingFinal, commentDtoList);
    }

    @Override
    public List<ItemDtoDated> findAllItemFromUser(long userId) {
        userValidatorService.byExistUser(userId);
        List<Item> items = itemRepository.findByOwnerId(userId);
        if (items.isEmpty()) {
            throw new NotFoundException("Пользователь " + userId + " не является хозяином ни одной вещи");
        }

        List<ItemDtoDated> datedItemList = new ArrayList<>();
        List<Booking> lastBookings = bookingRepository.findLastBookingsByBookerId(userId, LocalDateTime.now());
        List<Booking> nextBookings = bookingRepository.findNextBookingsByBookerId(userId, LocalDateTime.now());
        List<Comment> comments = commentRepository.findCommentsForItemsByOwnerId(userId);

        for (Item item : items) {
            Booking lastBooking = null;
            Booking nextBooking = null;
            List<CommentDto> commentList = new ArrayList<>();
            if (!lastBookings.isEmpty()) {
                for (Booking booking : lastBookings) {
                    if (booking.getItem().equals(item)) {
                        lastBooking = booking;
                        break;
                    }
                }
            }
            if (!nextBookings.isEmpty()) {
                for (Booking booking : nextBookings) {
                    if (booking.getItem().equals(item)) {
                        nextBooking = booking;
                        break;
                    }
                }
            }
            if (!comments.isEmpty()) {
                for (Comment comment : comments) {
                    if (comment.getItem().equals(item)) {
                        commentList.add(CommentMapper.toCommentDto(comment));
                    }
                }
            }
            datedItemList.add(ItemMapper.toItemDtoDated(item, BookingMapper.toItemDtoBooking(lastBooking),
                    BookingMapper.toItemDtoBooking(nextBooking), commentList));
        }
        return datedItemList;
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        List<Item> itemList = itemRepository.findByNameOrDescription(text);
        return itemList.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(long userId, long id) {
        itemRepository.deleteByOwnerIdAndId(userId, id);
    }

    @Override
    @Transactional
    public CommentDto saveComment(long userId, long itemId, CommentDto commentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID" + userId + " не найден."));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с ID- " + itemId + "не найдена."));
        List<Booking> bookingList = bookingRepository.findAllByBookerIdAndItemId(
                userId, itemId, LocalDateTime.now());
        if (commentDto.getText().isBlank() || commentDto.getText() == null) {
            throw new ValidationException("нельзя отправлять пустой комментарий.");
        }
        if (bookingList.isEmpty()) {
            throw new ValidationException("Пользователь с ID- " + userId + " не может писать отзыв на вещь с ID- " +
                    itemId + ", т.к. не соблюдены условия. Отзыв может оставить только тот пользователь, который брал " +
                    "эту вещь в аренду, и только после окончания срока аренды.");
        }
        Comment comment = CommentMapper.toComment(commentDto, item, user);
        Comment commentFromRepository = commentRepository.save(comment);
        return CommentMapper.toCommentDto(commentFromRepository);
    }
}
