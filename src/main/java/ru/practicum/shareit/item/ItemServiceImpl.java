package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepositoryJpa;
import ru.practicum.shareit.exceptions.InternalServerException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mappers.CommentMapperMapStruct;
import ru.practicum.shareit.item.mappers.ItemMapperMapStruct;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepositoryJpa;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemMapperMapStruct itemMapperMapStruct;
    private final UserRepositoryJpa userRepositoryJpa;
    private final ItemRepositoryJpa itemRepositoryJpa;
    private final BookingRepositoryJpa bookingRepositoryJpa;
    private final CommentRepositoryJpa commentRepositoryJpa;
    private final CommentMapperMapStruct commentMapperMapStruct;

    @Override
    public ItemDto addItemJpa(Long userId, ItemDto itemDto) {
        User owner = userRepositoryJpa.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с " + userId + " не найден"));
        Item item = itemMapperMapStruct.fromItemDto(itemDto);
        item.setOwner(owner);
        Long id = itemRepositoryJpa.save(item).getId();
        if (id == null) {
            throw new InternalServerException("Не удалось сохранить данные");
        }
        item.setId(id);
        return itemMapperMapStruct.toItemDto(item);
    }

    @Override
    public ItemDto updateItemJpa(Long userId, ItemDto itemDto, Long itemId) {
        Item itemFrom = itemRepositoryJpa.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь с " + itemId + " не найдена"));
        if (!itemFrom.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Вещь не пренадлежит пользователю с id " + userId);
        }
        if (itemDto.getName() != null) {
            itemFrom.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            itemFrom.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            itemFrom.setAvailable(itemDto.getAvailable());
        }
        itemRepositoryJpa.save(itemFrom);
        return itemMapperMapStruct.toItemDto(itemFrom);
    }

    @Override
    public ItemDtoWithCommentAndDate getItemByIdJpa(long userId, Long itemId) {
        Item item = itemRepositoryJpa.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь с " + itemId + " не найдена"));
        ItemDtoWithCommentAndDate  itemDtoWithCommentAndDate= itemMapperMapStruct.toItemDtoWithCommentAndDate(item);
        if (item.getOwner().getId() != userId) {
            itemDtoWithCommentAndDate.setLastBooking(null);
            itemDtoWithCommentAndDate.setNextBooking(null);
        }
        return itemDtoWithCommentAndDate;
    }

    @Override
    public List<ItemDtoWithCommentAndDate> getItemsFromUsersJpa(Long userId) {
        return itemMapperMapStruct.toItemsDtoWithCommentAndDate(itemRepositoryJpa.findByOwnerId(userId));
    }

    @Override
    public List<ItemDto> searchJpa(String search, Long userId) {
        if (search.isEmpty()) {
            return new ArrayList<>();
        }
        return itemMapperMapStruct.toItemDtoList(itemRepositoryJpa.searchJpa(search));
    }

    @Override
    public CommentDtoInConsole addComment(CommentDtoFromConsole commentDtoFromConsole) {
        Item item = itemRepositoryJpa.findById(commentDtoFromConsole.getItemId()).orElseThrow(() -> new NotFoundException("Вещь с " + commentDtoFromConsole.getItemId() + " не найдена"));
        User author = userRepositoryJpa.findById(commentDtoFromConsole.getUserId()).orElseThrow(() -> new NotFoundException("Пользователь с " + commentDtoFromConsole.getUserId() + " не найден"));
        Comment comment = commentMapperMapStruct.inComment(commentDtoFromConsole);
        long checkForAddComment = bookingRepositoryJpa.checkForAddComment(commentDtoFromConsole.getItemId(), commentDtoFromConsole.getUserId(), Instant.now().plusSeconds(10800));
        if (checkForAddComment == 0) {
            throw new InternalServerException("Ошибка валидации при добавлении комментария. Возможно пользователь не брал вещь в аренду");
        }
        comment.setAuthor(author);
        comment.setItem(item);
        Long id = commentRepositoryJpa.save(comment).getId();
        comment.setId(id);
        return commentMapperMapStruct.fromComment(comment);
    }

}
