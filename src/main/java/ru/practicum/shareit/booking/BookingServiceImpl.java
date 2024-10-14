package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoFromConsole;
import ru.practicum.shareit.booking.dto.BookingDtoInConsole;
import ru.practicum.shareit.booking.mappers.BookingMapperMapStruct;
import ru.practicum.shareit.exceptions.InternalServerException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemRepositoryJpa;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepositoryJpa;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final ItemRepositoryJpa itemRepositoryJpa;
    private final UserRepositoryJpa userRepositoryJpa;
    private final BookingMapperMapStruct bookingMapperMapStruct;
    private final BookingRepositoryJpa bookingRepositoryJpa;

    @Override
    public BookingDtoInConsole addBookingJpa(BookingDtoFromConsole bookingDtoFromConsole, long userId) {
        if (bookingDtoFromConsole.getStart().equals(bookingDtoFromConsole.getEnd())) {
            throw new InternalServerException("Начало бронирования не должно совпадать с концом бронирования");
        }
        long checkIntersection = bookingRepositoryJpa.checkIntersection(bookingDtoFromConsole.getItemId(), bookingDtoFromConsole.getStart().toInstant(ZoneOffset.UTC), bookingDtoFromConsole.getEnd().toInstant(ZoneOffset.UTC));
        if (checkIntersection != 0) {
            throw new InternalServerException("Добавляемое бронирование пересекается с имеющимися бронированиями");
        }
        Item item = itemRepositoryJpa.findById(bookingDtoFromConsole.getItemId()).orElseThrow(() -> new NotFoundException("Вещь с " + bookingDtoFromConsole.getItemId() + " не найдена"));
        User booker = userRepositoryJpa.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с " + userId + " не найден"));
        Booking booking = bookingMapperMapStruct.fromBookingDtoFromConsole(bookingDtoFromConsole);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(Booking.BookingType.WAITING);
        if (!booking.getItem().getAvailable()) {
            throw new InternalServerException("Вещь с id " + item.getId() + " недоступна для бронирования");
        }
        Long id = bookingRepositoryJpa.save(booking).getId();
        booking.setId(id);
        return bookingMapperMapStruct.fromBooking(booking);
    }

    @Override
    public BookingDtoInConsole approved(long userId, long bookingId, boolean approved) {
        Booking booking = bookingRepositoryJpa.approved(userId, bookingId);
        if (booking == null) {
            throw new InternalServerException("Неверный идентификатор пользователя или бронирования");
        }
        if (booking.getStatus().equals(Booking.BookingType.WAITING)) {
            if (approved) {
                booking.setStatus(Booking.BookingType.APPROVED);
            } else {
                booking.setStatus(Booking.BookingType.REJECTED);
            }
        }
        bookingRepositoryJpa.save(booking);
        return bookingMapperMapStruct.fromBooking(booking);
    }

    @Override
    public BookingDtoInConsole getBookingById(long userId, long bookingId) {
        Booking booking = bookingRepositoryJpa.findById(bookingId).orElseThrow(() -> new NotFoundException("Бронирование с " + bookingId + " не найдено"));
        long bookerId = booking.getBooker().getId();
        long ownerId = booking.getItem().getOwner().getId();
        if (bookerId == userId || ownerId == userId) {
            return bookingMapperMapStruct.fromBooking(booking);
        } else {
            throw new InternalServerException("У пользователей нет прав на получение бронирования с id " + bookingId);
        }
    }

    @Override
    public List<BookingDtoInConsole> getBookingsUser(long userId, Booking.State bookingState) {
        userRepositoryJpa.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с " + userId + " не найден"));
        switch (bookingState) {
            case ALL -> {
                return bookingMapperMapStruct.fromBookings(bookingRepositoryJpa.findAllBookingsById(userId));
            }
            case CURRENT -> {
                return bookingMapperMapStruct.fromBookings(bookingRepositoryJpa.findCurrentBookingsById(userId, LocalDateTime.now().toInstant(ZoneOffset.UTC)));
            }
            case PAST -> {
                return bookingMapperMapStruct.fromBookings(bookingRepositoryJpa.findPastBookingsById(userId, LocalDateTime.now().toInstant(ZoneOffset.UTC)));
            }
            case FUTURE -> {
                return bookingMapperMapStruct.fromBookings(bookingRepositoryJpa.findFutureBookingsById(userId, LocalDateTime.now().toInstant(ZoneOffset.UTC)));
            }
            case WAITING -> {
                return bookingMapperMapStruct.fromBookings(bookingRepositoryJpa.findWaitingBookingsById(userId));
            }
            default -> {
                return bookingMapperMapStruct.fromBookings(bookingRepositoryJpa.findRejectedBookingsById(userId));
            }
        }
    }

    @Override
    public List<BookingDtoInConsole> getBookingsItemsUser(long userId, Booking.State bookingState) {
        Long countItemsUser = bookingRepositoryJpa.countItemsUser(userId);
        if (countItemsUser == 0) {
            throw new NotFoundException("Не найдены вещи для пользователя с id " + userId);
        }
        switch (bookingState) {
            case ALL -> {
                return bookingMapperMapStruct.fromBookings(bookingRepositoryJpa.findAllBookingsForItemsById(userId));
            }
            case CURRENT -> {
                return bookingMapperMapStruct.fromBookings(bookingRepositoryJpa.findCurrentBookingsForItemsById(userId, LocalDateTime.now().toInstant(ZoneOffset.UTC)));
            }
            case PAST -> {
                return bookingMapperMapStruct.fromBookings(bookingRepositoryJpa.findPastBookingsForItemsById(userId, LocalDateTime.now().toInstant(ZoneOffset.UTC)));
            }
            case FUTURE -> {
                return bookingMapperMapStruct.fromBookings(bookingRepositoryJpa.findFutureBookingsForItemsById(userId, LocalDateTime.now().toInstant(ZoneOffset.UTC)));
            }
            case WAITING -> {
                return bookingMapperMapStruct.fromBookings(bookingRepositoryJpa.findWaitingBookingsForItemsById(userId));
            }
            default -> {
                return bookingMapperMapStruct.fromBookings(bookingRepositoryJpa.findRejectedBookingsForItemsById(userId));
            }
        }
    }
}
