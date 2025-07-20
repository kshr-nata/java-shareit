package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewItemRequestDto {
    @NotBlank(message = "Описание запроса не может быть пустым")
    String description;
}
