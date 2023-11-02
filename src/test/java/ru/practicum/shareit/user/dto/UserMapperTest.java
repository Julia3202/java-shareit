package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {
    private User user;
    private UserDto userDto;

    @BeforeEach
    void BeforeEach() {
        user = new User(1, "userName", "user@mail.ru");
        userDto = new UserDto(1L, "userName", "user@mail.ru");
    }

    @Test
    void toUserDto() {
        UserDto userDtoTest = UserMapper.toUserDto(user);
        assertEquals(user.getId(), userDtoTest.getId());
        assertEquals(user.getName(), userDtoTest.getName());
        assertEquals(user.getEmail(), userDtoTest.getEmail());
    }

    @Test
    void toUser() {
        User userTest = UserMapper.toUser(userDto);
        assertEquals(userDto.getId(), userTest.getId());
        assertEquals(userDto.getName(), userTest.getName());
        assertEquals(userDto.getEmail(), userTest.getEmail());
    }

    @Test
    void testToUser() {
        User userTest = UserMapper.toUser(userDto, user);
        assertEquals(userDto.getId(), userTest.getId());
        assertEquals(userDto.getName(), userTest.getName());
        assertEquals(userDto.getEmail(), userTest.getEmail());
    }

    @Test
    void toUserDtoBooking() {
        UserDtoBooking userTest = UserMapper.toUserDtoBooking(user);
        assertEquals(user.getId(), userTest.getId());
    }
}