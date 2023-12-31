package ru.practicum.shareit.user.dto;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

@Data
public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail()
        );
    }

    public static User toUser(UserDto userDto, User user) {
        return new User(
                userDto.getId(),
                userDto.getName() != null ? userDto.getName() : user.getName(),
                userDto.getEmail() != null ? userDto.getEmail() : user.getEmail()
        );
    }

    public static UserDtoBooking toUserDtoBooking(User booker) {
        return new UserDtoBooking(booker.getId());
    }
}
