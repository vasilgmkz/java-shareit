package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class CommentDtoFromConsole {
    String text;
    long userId;
    long itemId;
}
