package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public Collection<ItemDto> findItemsByUserId(int userId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("Пользователь с id " + userId + " не найден."));
        List<Item> userItems = itemRepository.findAllWithCommentsByOwner(userId);
        return ItemMapper.toItemDto(userItems);
    }

    @Override
    public ItemDto create(int userId, Item item) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("Пользователь с id " + userId + " не найден."));
        item.setOwner(user);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto update(int userId, int itemId, ItemUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("Пользователь с id " + userId + " не найден."));
        Item item = itemRepository.findById(itemId).orElseThrow(()
        -> new NotFoundException("Вещь с id " + itemId + " не найдена."));
        item = ItemMapper.updateItemFields(item, request);
        Item newItem = itemRepository.save(item);
        return ItemMapper.toItemDto(newItem);
    }

    @Override
    public ItemDto findItemById(int itemId) {
        Item item = itemRepository.findOneWithComments(itemId).orElseThrow(()
                -> new NotFoundException("Вещь с id " + itemId + " не найдена."));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        if (text.isEmpty()) {
            return List.of();
        }
        return itemRepository.searchItems(text).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public Comment createComment(int userId, int itemId, NewCommentRequest request) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("Пользователь с id " + userId + " не найден."));
        Item item = itemRepository.findById(itemId).orElseThrow(()
                -> new NotFoundException("Вещь с id " + itemId + " не найдена."));
        Comment comment = CommentMapper.mapToComment(request,user, item);
        return commentRepository.save(comment);
    }
}
