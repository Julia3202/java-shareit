package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserValidatorServiceTest {
    private UserValidatorService userValidatorService;
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        userRepository = mock(UserRepository.class);
        userValidatorService = new UserValidatorService(userRepository);
    }

    @Test
    void byExistUser() {
        User user = new User(1, "userName", "user@mail.ru");
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        User userTest = userValidatorService.existUserById(1L);
        assertEquals(user.getId(), userTest.getId());
        assertEquals(user.getName(), userTest.getName());
        assertEquals(user.getEmail(), userTest.getEmail());
    }
}