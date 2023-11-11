package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.model.RequestDto;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/requests")
public class RequestController {
    private static final String USER_ID = "X-Sharer-User-Id";
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader(USER_ID) long userId, @RequestBody RequestDto requestDto) {
        return requestClient.createRequest(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> findRequestByUserId(@RequestHeader(USER_ID) long userId) {
        return requestClient.findRequestByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllRequests(@RequestHeader(USER_ID) long userId,
                                                  @RequestParam(defaultValue = "0") int from,
                                                  @RequestParam(defaultValue = "25") int size) {
        return requestClient.findAllRequests(userId, from, size);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> findRequestById(@RequestHeader(USER_ID) long userId, @PathVariable("requestId") long requestId) {
        return requestClient.findRequestById(userId, requestId);
    }
}
