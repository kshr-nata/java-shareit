package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingInfo;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.item.model.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemMapper {

    public static Item mapToItem(NewItemRequest request, ItemRequest itemRequest, User user) {
        Item item = new Item();
        item.setOwner(user);
        item.setName(request.getName());
        item.setRequest(itemRequest);
        item.setDescription(request.getDescription());
        item.setAvailable(request.getAvailable());
        return item;
    }

    public static ItemDto toItemDto(Item item) {
        Set<String> commentTexts = item.getComments().stream()
                .map(Comment::getText)
                .collect(Collectors.toSet());
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                commentTexts
        );
    }

    public static ItemDtoWithBookingsInfo toItemDtoWithBookingInfo(
            Item item, BookingInfo lastBooking, BookingInfo nextBooking) {
        Set<String> commentTexts = item.getComments().stream()
                .map(Comment::getText)
                .collect(Collectors.toSet());
        return new ItemDtoWithBookingsInfo(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBooking,
                nextBooking,
                commentTexts
        );
    }

    public static List<ItemDto> toItemDto(Iterable<Item> items) {
        List<ItemDto> dtos = new ArrayList<>();
        for (Item item : items) {
            dtos.add(toItemDto(item));
        }
        return dtos;
    }

    public static Item updateItemFields(Item item, ItemUpdateRequest request) {
        if (request.hasAvailable()) {
            item.setAvailable(request.getAvailable());
        }
        if (request.hasDescription()) {
            item.setDescription(request.getDescription());
        }
        if (request.hasName()) {
            item.setName(request.getName());
        }
        return item;
    }

}
