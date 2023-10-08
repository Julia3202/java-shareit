package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        User userFromRepository = userRepository.create(user);
        return UserMapper.toUserDto(userFromRepository);
    }

    @Override
    public UserDto update(UserDto userDto, long id) {
        User user = userRepository.update(userDto, id);
        User userFromDto = UserMapper.toUser(userDto, user);
        userFromDto.setId(id);
        return UserMapper.toUserDto(userFromDto);
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
        User user = userRepository.findById(id);
        return UserMapper.toUserDto(user);
    }

    @Override
    public void removeUser(long id) {
        userRepository.removeUser(id);
    }
}
