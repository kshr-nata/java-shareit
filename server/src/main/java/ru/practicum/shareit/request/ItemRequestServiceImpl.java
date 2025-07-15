package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemRequestDto create(int userId, NewItemRequestDto request) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("Пользователь с id " + userId + " не найден."));
        ItemRequest itemRequest = itemRequestRepository.save(ItemRequestMapper.toItemRequest(request, user));
        return ItemRequestMapper.toItemRequestDto(itemRequest, new HashSet<>());
    }

    @Override
    public List<ItemRequestDto> getRequestsByUser(int userId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("Пользователь с id " + userId + " не найден."));
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestorIdOrderByCreatedDesc(userId);
        List<ItemRequestDto> result = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            List<Item> items = itemRepository.findByRequestId(itemRequest.getId());
            Set<ItemDto> itemsDto = new HashSet<>(ItemMapper.toItemDto(items));
            result.add(ItemRequestMapper.toItemRequestDto(itemRequest, itemsDto));
        }
        return result;
    }

    @Override
    public List<ItemRequestDto> getAll() {
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByOrderByCreatedDesc();
        List<ItemRequestDto> result = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            List<Item> items = itemRepository.findByRequestId(itemRequest.getId());
            Set<ItemDto> itemsDto = new HashSet<>(ItemMapper.toItemDto(items));
            result.add(ItemRequestMapper.toItemRequestDto(itemRequest, itemsDto));
        }
        return result;
    }

    @Override
    public ItemRequestDto getById(int requestId) {
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(()
                -> new NotFoundException("Запрос с id " + requestId + " не найден."));
        List<Item> items = itemRepository.findByRequestId(requestId);
        Set<ItemDto> itemsDto = new HashSet<>(ItemMapper.toItemDto(items));
        return ItemRequestMapper.toItemRequestDto(itemRequest, itemsDto);
    }
}
