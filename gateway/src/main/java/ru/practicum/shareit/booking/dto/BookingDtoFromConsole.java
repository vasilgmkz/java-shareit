package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Slf4j
public class BookingDtoFromConsole {
    @NotNull
    Long itemId;
    @NotNull
    @FutureOrPresent
    LocalDateTime start;
    @NotNull
    @FutureOrPresent
    LocalDateTime end;

    @AssertTrue(message = "Начало бронирования не должно совпадать с концом бронирования")
    boolean isStartEqualsEnd() {return !start.equals(end);}

    @AssertTrue(message = "Начало бронирования не должно быть позже конца бронирования")
    boolean isStartBeforeEnd() {return start.isBefore(end);}
}
