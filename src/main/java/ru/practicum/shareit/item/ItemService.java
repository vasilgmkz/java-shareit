package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

interface ItemService {

    ItemDto updateItemJpa(Long userId, ItemDto itemDto, Long itemId);

    List<ItemDto> searchJpa(String search, Long userId);

    ItemDto addItemJpa(Long userId, ItemDto itemDto);

    ItemDtoWithCommentAndDate getItemByIdJpa(long userId, Long itemId);

    List<ItemDtoWithCommentAndDate> getItemsFromUsersJpa(Long userId);

    CommentDtoInConsole addComment(CommentDtoFromConsole commentDtoFromConsole);
}
