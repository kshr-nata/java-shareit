package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemStorage {
    Collection<Integer> findItemsByUserId(int userId);
    Optional<Item> findItemById(int id);
    Item create(int userId, Item item);
    Collection<Item> searchItems(String text);
}
