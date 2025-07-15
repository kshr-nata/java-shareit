package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * TODO Sprint add-item-requests.
 */
@AllArgsConstructor
@Data
public class ItemRequestDto {
    Integer id;
    String description;
    User requestor;
    LocalDateTime created;
    Set<ItemDto> items;
}
