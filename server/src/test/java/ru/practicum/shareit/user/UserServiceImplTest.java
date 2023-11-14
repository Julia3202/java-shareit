package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserServiceImplTest {

    private final EntityManager entityManager;
    private final UserService userService;
    private UserDto userDto;

    @BeforeEach
    void beforeEach() {
        userDto = new UserDto(1, "userName", "user@mail.ru");
    }

    @Test
    void create() {
        userService.create(userDto);
        TypedQuery<User> query = entityManager.createQuery("select u from User u where u.id = :id", User.class);
        User user = query.setParameter("id", 1L).getSingleResult();
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
    }

    @Test
    void update() {
        userService.create(userDto);
        userDto.setEmail("users@mail.ru");
        userService.update(userDto, 1);
        TypedQuery<User> query = entityManager.createQuery("select u from User u where u.id = :id", User.class);
        User user = query.setParameter("id", 1L).getSingleResult();
        assertEquals("users@mail.ru", user.getEmail());

    }

    @Test
    void findAll() {
        userService.create(userDto);
        List<UserDto> userDtoList = userService.findAll();
        assertEquals(1, userDtoList.size());
    }

    @Test
    void findById() {
        userService.create(userDto);
        UserDto userDtoTest = userService.findById(userDto.getId());
        assertEquals(userDtoTest, userDto);
    }

    @Test
    void removeUser() {
        userService.create(userDto);
        userService.removeUser(userDto.getId());
        List<UserDto> userDtoList = userService.findAll();
        assertEquals(0, userDtoList.size());
    }
}