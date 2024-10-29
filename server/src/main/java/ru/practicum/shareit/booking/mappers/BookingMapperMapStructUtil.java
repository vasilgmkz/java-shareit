package ru.practicum.shareit.booking.mappers;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mappers.ItemMapperMapStruct;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mappers.UserMapperMapStruct;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@Named("BookingMapperMapStructUtil")
@RequiredArgsConstructor
public class BookingMapperMapStructUtil {

    private final ItemMapperMapStruct itemMapperMapStruct;
    private final UserMapperMapStruct userMapperMapStruct;

    @Named("inUserDto")
    UserDto inUserDto(User user) {
        return userMapperMapStruct.toUserDto(user);
    }

    @Named("inItemDto")
    ItemDto inItemDto(Item item) {
        return itemMapperMapStruct.toItemDto(item);
    }

    @Named("inInstant")
    Instant inInstant(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.UTC);
    }


    @Named("fromInstant")
    LocalDateTime fromInstant(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}