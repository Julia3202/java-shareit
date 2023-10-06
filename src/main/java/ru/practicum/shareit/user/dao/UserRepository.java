package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    User create(User user);

    User update(UserDto userDto, long id);

    List<User> findAll();

    User findById(long id);

    void removeUser(long id);
}
