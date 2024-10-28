package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDtoFromConsole;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validation.Marker;

@RestController
@RequestMapping(path = "/items")
@Validated
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(Marker.AddItem.class)
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @Valid ItemDto itemDto) {
        return itemClient.addItemJpa(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    @Validated(Marker.UpdateItem.class)
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @Valid ItemDto itemDto, @PathVariable("itemId") long itemId) {
        return itemClient.updateItemJpa(userId, itemDto, itemId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("itemId") long itemId) {
        return itemClient.getItemByIdJpa(userId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("itemId") long itemId, @RequestBody @Valid CommentDtoFromConsole commentDtoFromConsole) {
        return itemClient.addComment(userId, itemId, commentDtoFromConsole);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemsFromUsers(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.getItemsFromUsersJpa(userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> search(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(name = "text") String text) {
        return itemClient.searchJpa(userId, text);
    }

    @PostMapping("/{requestId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(Marker.AddItem.class)
    public ResponseEntity<Object> addItemWithRequest(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @Valid ItemDto itemDto, @PathVariable("requestId") long requestId) {
        return itemClient.addItemWithRequest(userId, itemDto, requestId);
    }
}
