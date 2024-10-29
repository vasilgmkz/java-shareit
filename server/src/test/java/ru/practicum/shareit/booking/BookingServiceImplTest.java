package ru.practicum.shareit.booking;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDtoFromConsole;
import ru.practicum.shareit.booking.dto.BookingDtoInConsole;
import ru.practicum.shareit.exceptions.InternalServerException;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTest {

    private final BookingService bookingService;

    BookingDtoFromConsole bookingDtoFromConsole;
    long userId = 3;

    @BeforeEach
    void setUp() {
        bookingDtoFromConsole = new BookingDtoFromConsole();
        bookingDtoFromConsole.setStart(LocalDateTime.of(2024, 10, 24, 10, 00, 00));
        bookingDtoFromConsole.setEnd(LocalDateTime.of(2024, 10, 24, 10, 10, 00));
        bookingDtoFromConsole.setItemId(2L);
    }

    @Test
    @DisplayName("Создание бронирования")
    void testAddBookingJpa() {
        bookingDtoFromConsole.setStart(LocalDateTime.of(2024, 10, 28, 8, 00, 00));
        bookingDtoFromConsole.setEnd(LocalDateTime.of(2024, 10, 28, 15, 00, 00));
        InternalServerException exception1 = Assertions.assertThrows(InternalServerException.class, () -> bookingService.addBookingJpa(bookingDtoFromConsole, userId - 2));
        assertEquals(exception1.getMessage(), "Добавляемое бронирование пересекается с имеющимися бронированиями");
        bookingDtoFromConsole.setStart(LocalDateTime.of(2024, 10, 28, 16, 00, 00));
        bookingDtoFromConsole.setEnd(LocalDateTime.of(2024, 10, 28, 18, 10, 00));
        bookingDtoFromConsole.setItemId(10L);
        NotFoundException exception2 = Assertions.assertThrows(NotFoundException.class, () -> bookingService.addBookingJpa(bookingDtoFromConsole, userId - 2));
        assertEquals(exception2.getMessage(), "Вещь с 10 не найдена");
        bookingDtoFromConsole.setItemId(2L);
        NotFoundException exception3 = Assertions.assertThrows(NotFoundException.class, () -> bookingService.addBookingJpa(bookingDtoFromConsole, userId + 7));
        assertEquals(exception3.getMessage(), "Пользователь с 10 не найден");
        InternalServerException exception4 = Assertions.assertThrows(InternalServerException.class, () -> bookingService.addBookingJpa(bookingDtoFromConsole, userId));
        assertEquals(exception4.getMessage(), "Вещь с id 2 недоступна для бронирования");
        bookingDtoFromConsole.setItemId(3L);
        BookingDtoInConsole bookingDtoInConsole = bookingService.addBookingJpa(bookingDtoFromConsole, userId);
        assertEquals(bookingDtoInConsole.getId(), 4);
    }

    @Test
    @DisplayName("Подтверждение бронирования")
    void testApproved() {
        InternalServerException exception = Assertions.assertThrows(InternalServerException.class, () -> bookingService.approved(2, 1, true));
        assertEquals(exception.getMessage(), "Неверный идентификатор пользователя или бронирования");
        BookingDtoInConsole bookingDtoInConsole1 = bookingService.approved(2, 2, true);
        assertEquals(bookingDtoInConsole1.getStatus(), Booking.BookingType.APPROVED);
        BookingDtoInConsole bookingDtoInConsole2 = bookingService.approved(1, 1, false);
        assertEquals(bookingDtoInConsole2.getStatus(), Booking.BookingType.REJECTED);
        BookingDtoInConsole bookingDtoInConsole3 = bookingService.approved(2, 2, true);
        assertEquals(bookingDtoInConsole3.getStatus(), Booking.BookingType.APPROVED);
    }

    @Test
    @DisplayName("Получение бронирования")
    void testGetBookingById() {
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> bookingService.getBookingById(1, 10));
        assertEquals(exception.getMessage(), "Бронирование с 10 не найдено");
        BookingDtoInConsole bookingDtoInConsole = bookingService.getBookingById(2, 1);
        assertEquals(bookingDtoInConsole.getStart(), LocalDateTime.of(2024, 10, 22, 11, 10, 00));
        InternalServerException exception2 = Assertions.assertThrows(InternalServerException.class, () -> bookingService.getBookingById(1, 2));
        assertEquals(exception2.getMessage(), "У пользователей нет прав на получение бронирования с id 2");
        BookingDtoInConsole bookingDtoInConsole1 = bookingService.getBookingById(1, 1);
        assertEquals(bookingDtoInConsole1.getStart(), LocalDateTime.of(2024, 10, 22, 11, 10, 00));
    }

    @Test
    @DisplayName("Получение бронирований текущего пользователя")
    void testGetBookingsUser() {
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> bookingService.getBookingsUser(10, Booking.State.ALL));
        assertEquals(exception.getMessage(), "Пользователь с 10 не найден");
        List<BookingDtoInConsole> all = bookingService.getBookingsUser(2, Booking.State.ALL);
        assertEquals(all.size(), 2);
        List<BookingDtoInConsole> current = bookingService.getBookingsUser(2, Booking.State.CURRENT);
        List<BookingDtoInConsole> past = bookingService.getBookingsUser(2, Booking.State.PAST);
        assertEquals(past.size(), 1);
        List<BookingDtoInConsole> future = bookingService.getBookingsUser(2, Booking.State.FUTURE);
        assertEquals(future.size(), 1);
        List<BookingDtoInConsole> waiting = bookingService.getBookingsUser(2, Booking.State.WAITING);
        assertEquals(waiting.size(), 2);
        List<BookingDtoInConsole> rejected = bookingService.getBookingsUser(2, Booking.State.REJECTED);
        assertEquals(rejected.size(), 0);
    }

    @Test
    @DisplayName("Получение списка бронирований для всех вещей текущего пользователя")
    void testGetBookingsItemsUser() {
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> bookingService.getBookingsItemsUser(4, Booking.State.ALL));
        assertEquals(exception.getMessage(), "Не найдены вещи для пользователя с id 4");
        List<BookingDtoInConsole> all = bookingService.getBookingsItemsUser(1, Booking.State.ALL);
        assertEquals(all.size(), 2);
        List<BookingDtoInConsole> current = bookingService.getBookingsItemsUser(2, Booking.State.CURRENT);
        List<BookingDtoInConsole> past = bookingService.getBookingsItemsUser(2, Booking.State.PAST);
        assertEquals(past.size(), 1);
        List<BookingDtoInConsole> future = bookingService.getBookingsItemsUser(2, Booking.State.FUTURE);
        assertEquals(future.size(), 0);
        List<BookingDtoInConsole> waiting = bookingService.getBookingsItemsUser(2, Booking.State.WAITING);
        assertEquals(waiting.size(), 1);
        List<BookingDtoInConsole> rejected = bookingService.getBookingsItemsUser(2, Booking.State.REJECTED);
        assertEquals(rejected.size(), 0);
    }
}

