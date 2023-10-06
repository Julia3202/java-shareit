package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody ItemDto itemDto,
                          @PathVariable("itemId") long itemId) {
        return itemService.update(userId, itemDto, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findItemById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("itemId") long itemId) {
        return itemService.findItemById(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> findAllItemFromUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.findAllItemFromUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }

    @DeleteMapping("/{id}")
    public void removeItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("id") long id) {
        itemService.removeItem(userId, id);
    }
}