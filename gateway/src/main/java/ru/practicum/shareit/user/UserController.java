package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody UserDto userDto) {
        return userClient.create(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@RequestBody UserDto userDto, @PathVariable("userId") long id) {
        return userClient.update(userDto, id);
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {
        return userClient.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") long id) {
        return userClient.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeUser(@PathVariable("id") long id) {
        return userClient.removeUser(id);
    }
}
