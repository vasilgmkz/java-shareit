package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDtoFromConsole;
import ru.practicum.shareit.booking.dto.BookingDtoInConsole;

import java.util.List;

public interface BookingService {
    BookingDtoInConsole addBookingJpa(BookingDtoFromConsole bookingDtoFromConsole, long userId);

    BookingDtoInConsole approved(long userId, long bookingId, boolean approved);

    BookingDtoInConsole getBookingById(long userId, long bookingId);

    List<BookingDtoInConsole> getBookingsUser(long userId, Booking.State bookingState);

    List<BookingDtoInConsole> getBookingsItemsUser(long userId, Booking.State bookingState);
}
