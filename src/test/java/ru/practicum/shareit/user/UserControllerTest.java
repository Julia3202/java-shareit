package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {
    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;
    private UserDto userDto;
    private final List<UserDto> userDtoList = new ArrayList<>();

    @BeforeEach
    void beforeEach() {
        userDto = new UserDto(1L, "userName", "user@mail.ru");
        userDtoList.add(userDto);
    }

    @Test
    void create() throws Exception {
        when(userService.create(any()))
                .thenReturn(userDto);
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", notNullValue()))
                .andExpect(jsonPath("$.email", notNullValue()));
        verify(userService, times(1))
                .create(any());
    }

    @Test
    void update() throws Exception {
        when(userService.update(any(), anyLong()))
                .thenReturn(userDto);
        mockMvc.perform(patch("/users/1")
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", notNullValue()))
                .andExpect(jsonPath("$.email", notNullValue()));
        verify(userService, times(1))
                .update(any(), anyLong());
    }

    @Test
    void findAll() throws Exception {
        when(userService.findAll())
                .thenReturn(userDtoList);
        mockMvc.perform(get("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].name", notNullValue()))
                .andExpect(jsonPath("$[0].email", notNullValue()));
        verify(userService, times(1))
                .findAll();
    }

    @Test
    void findById() throws Exception {
        when(userService.findById(anyLong()))
                .thenReturn(userDto);
        mockMvc.perform(get("/users/1")
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", notNullValue()))
                .andExpect(jsonPath("$.email", notNullValue()));
        verify(userService, times(1))
                .findById(anyLong());
    }

    @Test
    void removeUserById() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
        verify(userService, times(1))
                .removeUser(anyLong());
    }
}