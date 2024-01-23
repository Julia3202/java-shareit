package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class RequestServiceImplTest {

    private final EntityManager entityManager;
    private final RequestService requestService;
    private final ItemService itemService;
    private final UserService userService;

    private ItemDto itemDto;
    private UserDto userDto;
    private RequestDto requestDto;

    @BeforeEach
    void beforeEach() {
        LocalDateTime create = LocalDateTime.now().plusDays(5);
        userDto = new UserDto(1, "userName", "user@mail.ru");
        UserDto userDtoTwo = new UserDto(2, "user2Name", "user2@mail.ru");
        itemDto = new ItemDto(1, "itemName", "itemDescription", true, null);
        List<ItemDto> items = List.of(itemDto);
        requestDto = new RequestDto(1L, "requestDescription", items, create);
        userService.create(userDto);
        userService.create(userDtoTwo);
        itemService.create(1, itemDto);
    }

    @Test
    void createRequest() {
        requestService.createRequest(1, requestDto);
        TypedQuery<Request> query = entityManager.createQuery("select r from Request r where r.id = :id",
                Request.class);
        Request request = query.setParameter("id", 1L).getSingleResult();
        assertEquals(request.getDescription(), requestDto.getDescription());
        assertEquals(request.getCreated(), requestDto.getCreated());
    }

    @Test
    void findRequestByUserId() {
        requestService.createRequest(1, requestDto);
        List<RequestDto> requestDtoList = requestService.findRequestByUserId(1);
        TypedQuery<Request> query = entityManager.createQuery("select r from Request r where r.requestor.id = :id",
                Request.class);
        Request request = query.setParameter("id", userDto.getId()).getSingleResult();
        assertEquals(request.getId(), requestDtoList.get(0).getId());
        assertEquals(request.getDescription(), requestDtoList.get(0).getDescription());
    }

    @Test
    void findAllRequests() {
        requestService.createRequest(1, requestDto);
        List<RequestDto> requestList = requestService.findAllRequests(2, 0, 1);
        assertEquals(1, requestList.size());
    }

    @Test
    void findRequestById() {
        requestService.createRequest(1, requestDto);
        itemDto.setRequestId(requestDto.getId());
        itemService.update(1, itemDto, itemDto.getId());
        RequestDto requestDtoTest = requestService.findRequestById(1, 1);
        assertEquals(requestDto.getDescription(), requestDtoTest.getDescription());
    }
}