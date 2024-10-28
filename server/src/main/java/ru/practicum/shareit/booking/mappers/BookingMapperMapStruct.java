package ru.practicum.shareit.booking.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDtoFromConsole;
import ru.practicum.shareit.booking.dto.BookingDtoInConsole;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {BookingMapperMapStructUtil.class})
public interface BookingMapperMapStruct {

    @Mapping(target = "start", qualifiedByName = {"BookingMapperMapStructUtil", "inInstant"}, source = "start")
    @Mapping(target = "end", qualifiedByName = {"BookingMapperMapStructUtil", "inInstant"}, source = "end")
    Booking fromBookingDtoFromConsole(BookingDtoFromConsole bookingDtoFromConsole);

    @Mapping(target = "start", qualifiedByName = {"BookingMapperMapStructUtil", "fromInstant"}, source = "start")
    @Mapping(target = "end", qualifiedByName = {"BookingMapperMapStructUtil", "fromInstant"}, source = "end")
    @Mapping(target = "item", qualifiedByName = {"BookingMapperMapStructUtil", "inItemDto"}, source = "item")
    @Mapping(target = "booker", qualifiedByName = {"BookingMapperMapStructUtil", "inUserDto"}, source = "booker")
    BookingDtoInConsole fromBooking(Booking booking);

    List<BookingDtoInConsole> fromBookings(List<Booking> bookings);
}
