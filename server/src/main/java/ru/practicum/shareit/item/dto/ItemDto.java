package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    Integer id;
    String name;
    String description;
    boolean available;
    Set<String> comments;
}
