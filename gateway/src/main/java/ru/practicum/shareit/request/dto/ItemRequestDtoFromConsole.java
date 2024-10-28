package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Slf4j
public class ItemRequestDtoFromConsole {
    @NotEmpty
    private String description;
}
