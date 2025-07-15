package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateRequest;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> findAll() {
        return userClient.findAll();
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto request) {
        return userClient.create(request);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(
            @PathVariable int id,
            @Valid @RequestBody UserUpdateRequest request) {
        return userClient.update(id, request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findUserById(@PathVariable int id) {
        return userClient.findUserById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUserById(@PathVariable int id) {
        return userClient.deleteUserById(id);
    }
}