package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingInfo;

import java.util.Set;


public class ItemDtoWithBookingsInfo {
    Integer id;
    String name;
    String description;
    boolean available;
    BookingInfo lastBooking;
    BookingInfo nextBooking;
    Set<String> comments;
}
