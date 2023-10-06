package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Component
public interface UserRepository {

    User create(User user);

    User update(UserDto userDto, long id);

    List<User> findAll();

    User findById(long id);

    void removeUser(long id);
}
