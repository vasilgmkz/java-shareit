package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithCommentAndDate;
import ru.practicum.shareit.request.dto.ItemRequestDtoFromConsole;
import ru.practicum.shareit.request.dto.ItemRequestDtoInConsoleCreated;
import ru.practicum.shareit.request.dto.ItemRequestDtoInConsoleWithItems;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDtoInConsoleCreated addItemRequestJpa(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @Valid ItemRequestDtoFromConsole itemRequestDtoFromConsole) {
        return itemRequestService.addItemRequestJpa(itemRequestDtoFromConsole, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDtoInConsoleWithItems> getItemRequestsUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.getItemRequestsUserId(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDtoInConsoleCreated> getItemRequestsAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.getItemRequestsAll(userId);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDtoInConsoleWithItems getItemRequestId(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("requestId") long requestId) {
        return itemRequestService.getItemRequestId(requestId);
    }
}
