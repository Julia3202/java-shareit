package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.CommentDto;
import ru.practicum.shareit.item.model.ItemDto;

@AllArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private static final String USER_ID = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(USER_ID) long userId, @RequestBody ItemDto itemDto) {
        return itemClient.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(USER_ID) long userId, @RequestBody ItemDto itemDto,
                                         @PathVariable("itemId") long itemId) {
        return itemClient.update(userId, itemDto, itemId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findItemById(@RequestHeader(USER_ID) long userId,
                                               @PathVariable("itemId") long itemId) {
        return itemClient.findItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllItemFromUser(@RequestHeader(USER_ID) long userId) {
        return itemClient.findAllItemFromUser(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text) {
        return itemClient.search(text);
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader(USER_ID) long userId, @PathVariable("id") long id) {
        itemClient.deleteById(userId, id);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(USER_ID) long userId, @PathVariable long itemId,
                                             @RequestBody CommentDto commentDto) {
        return itemClient.saveComment(userId, itemId, commentDto);
    }
}
