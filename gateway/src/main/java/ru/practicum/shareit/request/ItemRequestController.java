package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.SharedHeaders;
import ru.practicum.shareit.request.dto.NewItemRequestDto;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestHeader(SharedHeaders.USER_ID_HEADER) int userId,
            @Valid @RequestBody NewItemRequestDto request) {
        return itemRequestClient.create(userId, request);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestsByUser(
            @RequestHeader(SharedHeaders.USER_ID_HEADER) int userId) {
        return itemRequestClient.getRequestsByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(
            @RequestHeader(SharedHeaders.USER_ID_HEADER) int userId,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemRequestClient.getAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(
            @RequestHeader(SharedHeaders.USER_ID_HEADER) int userId,
            @PathVariable int requestId) {
        return itemRequestClient.getById(userId, requestId);
    }
}