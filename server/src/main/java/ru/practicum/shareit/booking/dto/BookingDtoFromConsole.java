package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Slf4j
public class BookingDtoFromConsole {
    Long itemId;
    LocalDateTime start;
    LocalDateTime end;
}
