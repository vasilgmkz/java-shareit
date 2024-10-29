package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Slf4j
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
