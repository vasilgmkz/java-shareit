package ru.practicum.shareit.item.mappers;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@Named("CommentMapperMapStructUtil")
@RequiredArgsConstructor
public class CommentMapperMapStructUtil {

    @Named("fromInstant")
    LocalDateTime fromInstant(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}
