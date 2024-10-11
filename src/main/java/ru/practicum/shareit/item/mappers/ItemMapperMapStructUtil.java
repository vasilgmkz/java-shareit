package ru.practicum.shareit.item.mappers;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingRepositoryJpa;
import ru.practicum.shareit.item.CommentRepositoryJpa;
import ru.practicum.shareit.item.dto.CommentDtoInConsole;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.request.ItemRequest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
@Named("ItemMapperMapStructUtil")
@RequiredArgsConstructor
public class ItemMapperMapStructUtil {
    private final BookingRepositoryJpa bookingRepositoryJpa;
    private final CommentRepositoryJpa commentRepositoryJpa;
    private final CommentMapperMapStruct commentMapperMapStruct;


    @Named("requestConvertor")
    Long requestConvertor(ItemRequest request) {
        return request != null ? request.getId() : null;
    }

    @Named("getLastBooking")
    LocalDateTime getLastBooking(Long itemId) {
        Instant instant = bookingRepositoryJpa.findLastBookingsForItem(itemId, Instant.now().plusSeconds(10800));
        if (instant == null) {
            return null;
        }
        return fromInstant(instant);
    }

    @Named("getNearestBooking")
    LocalDateTime getNearestBooking(Long itemId) {
        Instant instant = bookingRepositoryJpa.findNearestBookingsForItem(itemId, Instant.now().plusSeconds(10800));
        if (instant == null) {
            return null;
        }
        return fromInstant(instant);
    }

    @Named("fromInstant")
    LocalDateTime fromInstant(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    @Named("getCommentsForItem")
    List<CommentDtoInConsole> getCommentsForItem(long itemId) {
        List<Comment> comments = commentRepositoryJpa.getCommentsForItem(itemId);
        return commentMapperMapStruct.fromComments(comments);
    }
}
