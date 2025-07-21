package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.SharedHeaders;
import ru.practicum.shareit.item.dto.*;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> findItemsByUser(@RequestHeader(SharedHeaders.USER_ID_HEADER) int userId) {
        return itemClient.findItemsByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(SharedHeaders.USER_ID_HEADER) int userId,
                                         @Valid @RequestBody NewItemRequest item) {
        return itemClient.create(userId, item);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestHeader(SharedHeaders.USER_ID_HEADER) int userId,
                                         @PathVariable int id,
                                         @Valid @RequestBody ItemUpdateRequest request) {
        return itemClient.update(userId, id, request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@RequestHeader(SharedHeaders.USER_ID_HEADER) int userId,
                                           @PathVariable int id) {
        return itemClient.findById(userId, id);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader(SharedHeaders.USER_ID_HEADER) int userId,
                                              @RequestParam String text) {
        return itemClient.searchItems(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(SharedHeaders.USER_ID_HEADER) int userId,
                                                @PathVariable int itemId,
                                                @Valid @RequestBody NewCommentRequest request) {
        return itemClient.createComment(userId, itemId, request);
    }
}