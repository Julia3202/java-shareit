package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.UserValidatorService;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final UserValidatorService userValidatorService;
    private final RequestValidator requestValidator = new RequestValidator();

    @Override
    @Transactional
    public RequestDto createRequest(long userId, RequestDto requestDto) {
        User user = userValidatorService.existUserById(userId);
        requestValidator.validDescription(requestDto);
        Request requestFromDto = RequestMapper.toRequest(requestDto, user);
        Request request = requestRepository.save(requestFromDto);
        return RequestMapper.toRequestDto(request, null);
    }

    @Override
    @Transactional
    public List<RequestDto> findRequestByUserId(long userId) {
        userValidatorService.existUserById(userId);
        List<Request> requests = requestRepository.findAllByRequestorIdOrderByCreatedDesc(userId);
        List<Long> requestIdList = requests.stream()
                .map(Request::getId)
                .collect(Collectors.toList());
        List<Item> items = itemRepository.findAllByRequestIdInOrderById(requestIdList);
        return createListRequestDto(requests, items);
    }

    @Override
    public List<RequestDto> findAllRequests(long userId, int from, int size) {
        requestValidator.validFrom(from);
        requestValidator.validSize(size);
        Pageable page = PageRequest.of(from, size);
        List<Request> requests = requestRepository.findAllByRequestorIdNot(userId, page).getContent();
        List<Long> requestIdList = requests.stream()
                .map(Request::getId)
                .collect(Collectors.toList());
        List<Item> items = itemRepository.findAllByRequestRequestorIdNotAndRequestIdInOrderById(userId, requestIdList);
        return createListRequestDto(requests, items);
    }

    @Override
    public RequestDto findRequestById(long userId, long requestId) {
        userValidatorService.existUserById(userId);
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с ID- " + requestId + " не найден."));
        List<ItemDto> itemDtoList = new ArrayList<>();
        List<Item> itemList = List.of(itemRepository.findByRequestIdOrderById(requestId));
        for (Item item : itemList) {
            if (item.getRequest().equals(request)) {
                itemDtoList.add(ItemMapper.toItemDto(item));
            }
        }
        return RequestMapper.toRequestDto(request, itemDtoList);
    }

    private List<RequestDto> createListRequestDto(List<Request> requests, List<Item> items) {
        List<RequestDto> requestDtoList = new ArrayList<>();
        for (Request request : requests) {
            List<ItemDto> itemDtoList = new ArrayList<>();
            if (items.isEmpty()) {
                requestDtoList.add((RequestMapper.toRequestDto(request, new ArrayList<>())));
            }
            for (Item item : items) {
                if (item.getRequest().equals(request)) {
                    itemDtoList.add(ItemMapper.toItemDto(item));
                }
                requestDtoList.add(RequestMapper.toRequestDto(request, itemDtoList));
            }
        }
        return requestDtoList;
    }
}
