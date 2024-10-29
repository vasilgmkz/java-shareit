package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoWithCommentAndDate {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long request;
    private List<CommentDtoInConsole> comments;
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
}
