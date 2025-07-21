package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.*;

import java.util.Collection;

public interface ItemService {

    Collection<ItemDtoWithBookingsInfo> findItemsByUserId(int userId);

    ItemDto create(int userId, NewItemRequest item);

    ItemDto update(int userId, int itemId, ItemUpdateRequest request);

    ItemDtoWithBookingsInfo findItemById(int itemId);

    Collection<ItemDto> searchItems(String text);

    CommentDto createComment(int userId, int itemId, NewCommentRequest request);
}
