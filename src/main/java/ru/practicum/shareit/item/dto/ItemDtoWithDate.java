package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoWithDate {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long request;
    private LocalDateTime lastBooking;
    private LocalDateTime nearestBooking;
}
