package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoFromConsole;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.validation.Marker;

@RestController
@RequestMapping(path = "/bookings")
@Validated
@RequiredArgsConstructor
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(Marker.AddBooking.class)
    public ResponseEntity<Object> addBookingJpa(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @Valid BookingDtoFromConsole bookingDtoFromConsole) {
        return bookingClient.addBookingJpa(bookingDtoFromConsole, userId);
    }

    @PatchMapping(("/{bookingId}"))
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> approved(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("bookingId") long bookingId, @RequestParam(name = "approved") boolean approved) {
        return bookingClient.approved(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getBookingById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("bookingId") long bookingId) {
        return bookingClient.getBookingById(userId, bookingId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getBookingsUser(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(defaultValue = "ALL", name = "state") State bookingState) {
        return bookingClient.getBookingsUser(userId, bookingState);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getBookingsItemsUser(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(defaultValue = "ALL", name = "state") State bookingState) {
        return bookingClient.getBookingsItemsUser(userId, bookingState);
    }
}