package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoFromConsole;
import ru.practicum.shareit.booking.dto.BookingDtoInConsole;
import ru.practicum.shareit.validation.Marker;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@Validated
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(Marker.AddBooking.class)
    public BookingDtoInConsole addBookingJpa(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @Valid BookingDtoFromConsole bookingDtoFromConsole) {
        return bookingService.addBookingJpa(bookingDtoFromConsole, userId);
    }

    @PatchMapping(("/{bookingId}"))
    @ResponseStatus(HttpStatus.OK)
    public BookingDtoInConsole approved(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("bookingId") long bookingId, @RequestParam(name = "approved") boolean approved) {
        return bookingService.approved(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDtoInConsole getBookingById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("bookingId") long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDtoInConsole> getBookingsUser(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(defaultValue = "ALL", name = "state") Booking.State bookingState) {
        return bookingService.getBookingsUser(userId, bookingState);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDtoInConsole> getBookingsItemsUser(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(defaultValue = "ALL", name = "state") Booking.State bookingState) {
        return bookingService.getBookingsItemsUser(userId, bookingState);
    }
}
