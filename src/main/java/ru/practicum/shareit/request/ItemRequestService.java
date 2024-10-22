package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDtoFromConsole;
import ru.practicum.shareit.request.dto.ItemRequestDtoInConsoleCreated;
import ru.practicum.shareit.request.dto.ItemRequestDtoInConsoleWithItems;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDtoInConsoleCreated addItemRequestJpa(ItemRequestDtoFromConsole itemRequestDtoFromConsole, long userId);

    List<ItemRequestDtoInConsoleWithItems> getItemRequestsUserId(long userId);

    List<ItemRequestDtoInConsoleCreated> getItemRequestsAll(long userId);

    ItemRequestDtoInConsoleWithItems getItemRequestId(long requestId);
}
