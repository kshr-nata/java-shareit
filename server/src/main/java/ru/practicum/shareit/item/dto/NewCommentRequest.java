package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewCommentRequest {
    @NotNull
    @NotEmpty
    private String text;
}
