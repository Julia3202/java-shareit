package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoDated;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private static final String USER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestHeader(USER_ID) long userId, @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(USER_ID) long userId, @RequestBody ItemDto itemDto,
                          @PathVariable("itemId") long itemId) {
        return itemService.update(userId, itemDto, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoDated findItemById(@RequestHeader(USER_ID) long userId, @PathVariable("itemId") long itemId) {
        return itemService.findItemById(userId, itemId);
    }

    @GetMapping
    public List<ItemDtoDated> findAllItemFromUser(@RequestHeader(USER_ID) long userId) {
        return itemService.findAllItemFromUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader(USER_ID) long userId, @PathVariable("id") long id) {
        itemService.deleteById(userId, id);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(USER_ID) long userId, @PathVariable long itemId,
                                 @RequestBody CommentDto commentDto) {
        return itemService.saveComment(userId, itemId, commentDto);
    }
}
