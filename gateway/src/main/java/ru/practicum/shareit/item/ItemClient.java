package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.*;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> findItemsByUserId(int userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> create(int userId, NewItemRequest item) {
        return post("", userId, item);
    }

    public ResponseEntity<Object> update(int userId, int id, ItemUpdateRequest request) {
        return patch("/" + id, userId, request);
    }

    public ResponseEntity<Object> findById(int userId, int id) {
        return get("/" + id, userId);
    }

    public ResponseEntity<Object> searchItems(int userId, String text) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("/search?text={text}", userId, parameters);
    }

    public ResponseEntity<Object> createComment(int userId, int itemId, NewCommentRequest request) {
        return post("/" + itemId + "/comment", userId, request);
    }
}