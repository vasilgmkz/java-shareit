package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentDtoFromConsole {
    @NotEmpty
    String text;
    long userId;
    long itemId;
}
