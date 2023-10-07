package ru.practicum.shareit.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.OtherException;
import ru.practicum.shareit.user.UserValidator;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.*;


@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserValidator userValidator = new UserValidator();
    private final Map<Long, User> userList = new HashMap<>();
    private final Set<String> emailList = new HashSet<>();
    private int count = 1;

    private int generateId() {
        return count++;
    }

    @Override
    public User create(User user) {
        userValidator.validate(user);
        if (userList.containsValue(user)) {
            log.error("Нельзя добавить пользователя,который был зарегистрирован раньше.");
            throw new NotFoundException("Пользователь не был найден.");
        }
        if (emailList.contains(user.getEmail())) {
            log.error("На одну почту можно зарегистрировать только одного пользователя.");
            throw new OtherException("На один адрес электронной почты можно зарегистрировать одного пользователя.");
        }
        long id = generateId();
        user.setId(id);
        userList.put(user.getId(), user);
        emailList.add(user.getEmail());
        return user;
    }

    @Override
    public User update(UserDto userDto, long id) {
        User user = findById(id);
        if (emailList.contains(userDto.getEmail())) {
            if (!userDto.getEmail().contains(user.getEmail())) {
                log.error("На одну почту можно зарегистрировать только одного пользователя.");
                throw new OtherException("На один адрес электронной почты можно зарегистрировать одного пользователя.");
            }
        }
        emailList.remove(user.getEmail());
        Optional.ofNullable(userDto.getName()).ifPresent(user::setName);
        Optional.ofNullable(userDto.getEmail()).ifPresent(user::setEmail);
        userList.put(user.getId(), user);
        emailList.add(user.getEmail());
        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userList.values());
    }

    @Override
    public User findById(long id) {
        User user = userList.get(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с ID" + id + " не найден.");
        }
        return user;
    }

    @Override
    public void removeUser(long id) {
        User user = findById(id);
        userList.remove(id);
        emailList.remove(user.getEmail());
    }
}
