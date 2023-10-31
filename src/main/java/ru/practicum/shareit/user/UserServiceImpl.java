package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator = new UserValidator();

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        userValidator.validate(user);
        User userFromRepository = userRepository.save(user);
        return UserMapper.toUserDto(userFromRepository);
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto, long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID" + id + " не найден."));
        User userFromDto = UserMapper.toUser(userDto, user);
        userFromDto.setId(id);
        User userFromRepository = userRepository.save(userFromDto);
        return UserMapper.toUserDto(userFromRepository);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID" + id + " не найден."));
        return UserMapper.toUserDto(user);
    }

    @Override
    public void removeUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID" + id + " не найден."));
        userRepository.delete(user);
    }
}
