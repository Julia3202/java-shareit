package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto, long id);

    List<UserDto> findAll();

    UserDto findById(long id);

    void removeUser(long id);
}
