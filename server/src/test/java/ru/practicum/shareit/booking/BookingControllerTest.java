package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDtoFromConsole;
import ru.practicum.shareit.booking.dto.BookingDtoInConsole;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@ExtendWith(MockitoExtension.class)
@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {
    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    private BookingDtoFromConsole bookingDtoFromConsole;

    private BookingDtoInConsole bookingDtoInConsole;


    long userId;
    Booking.State state;
    long bookingId;
    boolean approved;
    LocalDateTime start;
    LocalDateTime end;


    @BeforeEach
    void setUp() {
        userId = 1;
        state = Booking.State.ALL;
        bookingId = 1;
        approved = true;
        start = LocalDateTime.of(2024, 10, 24, 16, 43, 22);
        end = LocalDateTime.of(2024, 10, 24, 17, 43, 22);
        bookingDtoFromConsole = new BookingDtoFromConsole();
        bookingDtoFromConsole.setStart(start);
        bookingDtoInConsole = new BookingDtoInConsole();
        bookingDtoInConsole.setStart(start);
        bookingDtoInConsole.setId(1L);
        bookingDtoInConsole.setStatus(Booking.BookingType.WAITING);
    }

    @Test
    void testAddBookingJpa() throws Exception {
        when(bookingService.addBookingJpa(bookingDtoFromConsole, userId)).thenReturn(bookingDtoInConsole);
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .content(mapper.writeValueAsString(bookingDtoFromConsole))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":1,\"start\":\"2024-10-24T16:43:22\",\"end\":null,\"status\":\"WAITING\",\"booker\":null,\"item\":null}"))
                .andExpect(jsonPath("$.start", is(start.toString())))
                .andExpect(jsonPath("$.id", is(1)));
        verify(bookingService, times(1)).addBookingJpa(bookingDtoFromConsole, userId);
    }

    @Test
    void testApproved() throws Exception {
        bookingDtoInConsole.setStatus(Booking.BookingType.APPROVED);
        when(bookingService.approved(userId, bookingId, approved)).thenReturn(bookingDtoInConsole);
        mvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"start\":\"2024-10-24T16:43:22\",\"end\":null,\"status\":\"APPROVED\",\"booker\":null,\"item\":null}"))
                .andExpect(jsonPath("$.id", is(1)));
        verify(bookingService, times(1)).approved(userId, bookingId, approved);
    }

    @Test
    void testGetBookingById() throws Exception {
        bookingDtoInConsole.setStatus(Booking.BookingType.APPROVED);
        when(bookingService.getBookingById(userId, bookingId)).thenReturn(bookingDtoInConsole);
        mvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"start\":\"2024-10-24T16:43:22\",\"end\":null,\"status\":\"APPROVED\",\"booker\":null,\"item\":null}"))
                .andExpect(jsonPath("$.id", is(1)));
        verify(bookingService, times(1)).getBookingById(userId, bookingId);
    }

    @Test
    void testGetBookingsUser() throws Exception {
        bookingDtoInConsole.setStatus(Booking.BookingType.APPROVED);
        when(bookingService.getBookingsUser(userId, state)).thenReturn(List.of(bookingDtoInConsole));
        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", String.valueOf(state))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"start\":\"2024-10-24T16:43:22\",\"end\":null,\"status\":\"APPROVED\",\"booker\":null,\"item\":null}]"))
                .andExpect(jsonPath("$.[0].id", is(1)));
        verify(bookingService, times(1)).getBookingsUser(userId, state);
    }

    @Test
    void testGetBookingsItemsUser() throws Exception {
        bookingDtoInConsole.setStatus(Booking.BookingType.APPROVED);
        when(bookingService.getBookingsItemsUser(userId, state)).thenReturn(List.of(bookingDtoInConsole));
        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", String.valueOf(state))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"start\":\"2024-10-24T16:43:22\",\"end\":null,\"status\":\"APPROVED\",\"booker\":null,\"item\":null}]"))
                .andExpect(jsonPath("$.[0].id", is(1)));
        verify(bookingService, times(1)).getBookingsItemsUser(userId, state);
    }
}


