package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoFromConsole;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addItemRequestJpa(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @Valid ItemRequestDtoFromConsole itemRequestDtoFromConsole) {
        return requestClient.addItemRequestJpa(itemRequestDtoFromConsole, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemRequestsUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        return requestClient.getItemRequestsUserId(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemRequestsAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return requestClient.getItemRequestsAll(userId);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemRequestId(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("requestId") long requestId) {
        return requestClient.getItemRequestId(requestId, userId);
    }
}
