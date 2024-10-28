package ru.practicum.shareit.request.mappers;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@Named("ItemRequestMapperMapStructUtil")
@RequiredArgsConstructor
public class ItemRequestMapperMapStructUtil {
    @Named("fromInstant")
    LocalDateTime fromInstant(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}
