package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import java.util.Collection;

@RestController
@RequestMapping("/items")
public class  ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public Collection<ItemDtoWithBookingsInfo> findItemsByUser(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.findItemsByUserId(userId);
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") int userId,
                          @Valid @RequestBody NewItemRequest item) {
        return itemService.create(userId, item);

    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") int userId,
                          @PathVariable int id,
                          @Valid @RequestBody ItemUpdateRequest request) {
        return itemService.update(userId, id, request);
    }

    @GetMapping("/{id}")
    public ItemDtoWithBookingsInfo findById(@PathVariable int id) {
        return itemService.findItemById(id);
    }

    @GetMapping("search")
    public Collection<ItemDto> searchItems(@RequestParam String text) {
        return itemService.searchItems(text);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                    @PathVariable int itemId,
                                    @Valid @RequestBody NewCommentRequest request) {
        return itemService.createComment(userId, itemId, request);
    }
}
