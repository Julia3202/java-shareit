package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.model.RequestDto;

import java.util.Map;

@Service
public class RequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";
    private final RequestValidator requestValidator = new RequestValidator();

    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createRequest(long userId, RequestDto requestDto) {
        requestValidator.validDescription(requestDto);
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> findRequestByUserId(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> findAllRequests(long userId, int from, int size) {
        requestValidator.validFrom(from);
        requestValidator.validSize(size);
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all", userId, parameters);
    }

    public ResponseEntity<Object> findRequestById(long userId, long requestId) {
        return get("/" + requestId, userId);
    }
}
