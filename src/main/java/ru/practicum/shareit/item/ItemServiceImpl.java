package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.item.dto.ItemMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage, UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Collection<ItemDto> findItemsByUserId(int userId) {
        User user = userStorage.findUserById(userId).orElseThrow(()
                -> new NotFoundException("Пользователь с id " + userId + " не найден."));
        return itemStorage.findItemsByUserId(userId)
                .stream()
                .map(itemId -> {
                    Item item = itemStorage.findItemById(itemId).orElseThrow(()
                            -> new NotFoundException("Предмет с id " + itemId + " не найден"));
                    return ItemMapper.toItemDto(item);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto create(int userId, Item item) {
        User user = userStorage.findUserById(userId).orElseThrow(()
                -> new NotFoundException("Пользователь с id " + userId + " не найден."));
        item.setOwner(user);
        item = itemStorage.create(userId, item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(int userId, int itemId, ItemUpdateRequest request) {
        User user = userStorage.findUserById(userId).orElseThrow(()
                -> new NotFoundException("Пользователь с id " + userId + " не найден."));
        Item item = itemStorage.findItemById(itemId).orElseThrow(()
        -> new NotFoundException("Вещь с id " + itemId + " не найдена."));
        item = ItemMapper.updateItemFields(item, request);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto findItemById(int itemId) {
        Item item = itemStorage.findItemById(itemId).orElseThrow(()
                -> new NotFoundException("Вещь с id " + itemId + " не найдена."));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        if (text.isEmpty()) {
            return List.of();
        }
        return itemStorage.searchItems(text).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }
}
