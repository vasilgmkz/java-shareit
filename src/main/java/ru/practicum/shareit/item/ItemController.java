package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.validation.Marker;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@Validated
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(Marker.AddItem.class)
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @Valid ItemDto itemDto) {
        return itemService.addItemJpa(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    @Validated(Marker.UpdateItem.class)
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @Valid ItemDto itemDto, @PathVariable("itemId") long itemId) {
        return itemService.updateItemJpa(userId, itemDto, itemId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDtoWithCommentAndDate getItemById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("itemId") long itemId) {
        return itemService.getItemByIdJpa(userId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDtoInConsole addComment(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("itemId") long itemId, @RequestBody @Valid CommentDtoFromConsole commentDtoFromConsole) {
        commentDtoFromConsole.setItemId(itemId);
        commentDtoFromConsole.setUserId(userId);
        return itemService.addComment(commentDtoFromConsole);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDtoWithCommentAndDate> getItemsFromUsers(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItemsFromUsersJpa(userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> search(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(name = "text") String text) {
        return itemService.searchJpa(text, userId);
    }
}
