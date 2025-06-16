package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Slf4j
@Component
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Integer, List<Integer>> itemsByUsers = new HashMap<>();
    private final Map<Integer, Item> items = new HashMap<>();

    @Override
    public Collection<Integer> findItemsByUserId(int userId) {
        return itemsByUsers.get(userId);
    }

    @Override
    public Optional<Item> findItemById(int id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Item create(int userId, Item item) {
        item.setId(getNextId());
        items.put(item.getId(), item);
        List<Integer> userItems = itemsByUsers.getOrDefault(userId, new ArrayList<>());
        userItems.add(item.getId());
        itemsByUsers.put(userId, userItems);
        return item;
    }

    @Override
    public Collection<Item> searchItems(String text) {
        if (text == null) return List.of(); // или можно бросить исключение

        String lowerText = text.toLowerCase();
        return items.values().stream()
                .filter(item -> item.getAvailable()
                        && (Optional.ofNullable(item.getName()).orElse("").toLowerCase().contains(lowerText)
                        || Optional.ofNullable(item.getDescription()).orElse("").toLowerCase().contains(lowerText)))
                .toList();
    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    private int getNextId() {
        int currentMaxId = items.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
