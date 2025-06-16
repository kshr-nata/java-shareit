package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateRequest;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    Collection<ItemDto> findItemsByUserId(int userId);
    ItemDto create(int userId, Item item);
    ItemDto update(int userId, int itemId, ItemUpdateRequest request);
    ItemDto findItemById(int itemId);
    Collection<ItemDto> searchItems(String text);
}
