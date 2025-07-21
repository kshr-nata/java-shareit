package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.SharedHeaders;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public ItemRequestDto create(@RequestHeader(SharedHeaders.USER_ID_HEADER) int userId,
                                 @RequestBody NewItemRequestDto request) {
        return itemRequestService.create(userId, request);
    }

    @GetMapping
    public List<ItemRequestDto> getRequestsByUser(@RequestHeader(SharedHeaders.USER_ID_HEADER) int userId) {
        return itemRequestService.getRequestsByUser(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll() {
        return itemRequestService.getAll();
    }

    @GetMapping("{requestId}")
    public ItemRequestDto getById(@PathVariable int requestId) {
        return itemRequestService.getById(requestId);
    }

}
