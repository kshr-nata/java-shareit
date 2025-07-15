package ru.practicum.shareit.request;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto create(int userId, NewItemRequestDto request);

    List<ItemRequestDto> getRequestsByUser(int userId);

    List<ItemRequestDto> getAll();

    ItemRequestDto getById(int requestId);
}
