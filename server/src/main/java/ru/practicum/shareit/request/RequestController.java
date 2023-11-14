package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/requests")
public class RequestController {
    private static final String USER_ID = "X-Sharer-User-Id";
    private final RequestService requestService;

    @PostMapping
    public RequestDto createRequest(@RequestHeader(USER_ID) long userId, @RequestBody RequestDto requestDto) {
        return requestService.createRequest(userId, requestDto);
    }

    @GetMapping
    public List<RequestDto> findRequestByUserId(@RequestHeader(USER_ID) long userId) {
        return requestService.findRequestByUserId(userId);
    }

    @GetMapping("/all")
    public List<RequestDto> findAllRequests(@RequestHeader(USER_ID) long userId,
                                            @RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "25") int size) {
        return requestService.findAllRequests(userId, from, size);
    }

    @GetMapping("{requestId}")
    public RequestDto findRequestById(@RequestHeader(USER_ID) long userId, @PathVariable("requestId") long requestId) {
        return requestService.findRequestById(userId, requestId);
    }
}
