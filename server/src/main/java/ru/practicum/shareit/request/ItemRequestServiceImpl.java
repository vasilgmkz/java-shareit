package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemRepositoryJpa;
import ru.practicum.shareit.item.dto.ItemDtoForItemRequest;
import ru.practicum.shareit.item.mappers.ItemMapperMapStruct;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDtoFromConsole;
import ru.practicum.shareit.request.dto.ItemRequestDtoInConsoleCreated;
import ru.practicum.shareit.request.dto.ItemRequestDtoInConsoleWithItems;
import ru.practicum.shareit.request.mappers.ItemRequestMapperMapStruct;
import ru.practicum.shareit.user.UserRepositoryJpa;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepositoryJpa itemRequestRepositoryJpa;
    private final ItemRequestMapperMapStruct itemRequestMapperMapStruct;
    private final UserRepositoryJpa userRepositoryJpa;
    private final ItemRepositoryJpa itemRepositoryJpa;
    private final ItemMapperMapStruct itemMapperMapStruct;

    @Override
    public ItemRequestDtoInConsoleCreated addItemRequestJpa(ItemRequestDtoFromConsole itemRequestDtoFromConsole, long userId) {
        User user = userRepositoryJpa.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с " + userId + " не найден"));
        ItemRequest itemRequest = itemRequestMapperMapStruct.inItemRequest(itemRequestDtoFromConsole, user);
        Long id = itemRequestRepositoryJpa.save(itemRequest).getId();
        itemRequest.setId(id);
        return itemRequestMapperMapStruct.inItemRequestDtoInConsole(itemRequest);
    }

    @Override
    public List<ItemRequestDtoInConsoleWithItems> getItemRequestsUserId(long userId) {
        List<Item> items = itemRepositoryJpa.getItemRequestsUserId(userId);
        List<ItemRequest> itemRequests = itemRequestRepositoryJpa.findByRequestorId(userId);
        Map<ItemRequest, List<Item>> itemRequestListMap = new HashMap<>();
        for (ItemRequest itemRequest : itemRequests) {
            itemRequestListMap.put(itemRequest, new ArrayList<>());
        }
        for (Item item : items) {
            itemRequestListMap.get(item.getRequest()).add(item);
        }
        return itemRequestListMap.entrySet().stream().map(a -> {
            ItemRequestDtoInConsoleWithItems x = itemRequestMapperMapStruct.inItemRequestDtoInConsoleWithItems(a.getKey());
            List<ItemDtoForItemRequest> y = a.getValue().stream().map(itemMapperMapStruct::inItemDtoForItemRequest).toList();
            x.setItems(y);
            return x;
        }).sorted(((o1, o2) -> o2.getCreated().compareTo(o1.getCreated()))).collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDtoInConsoleCreated> getItemRequestsAll(long userId) {
        return itemRequestRepositoryJpa.getItemRequestsAll(userId).stream()
                .sorted((o1, o2) -> o2.getCreated().compareTo(o1.getCreated()))
                .map(itemRequestMapperMapStruct::inItemRequestDtoInConsole).toList();
    }

    @Override
    public ItemRequestDtoInConsoleWithItems getItemRequestId(long requestId) {
        ItemRequest itemRequest = itemRequestRepositoryJpa.findById(requestId).orElseThrow(() -> new NotFoundException("Запрос с " + requestId + " не найден"));
        List<Item> items = itemRepositoryJpa.getItemRequestsId(requestId);
        ItemRequestDtoInConsoleWithItems itemRequestDtoInConsoleWithItems = itemRequestMapperMapStruct.inItemRequestDtoInConsoleWithItems(itemRequest);
        List<ItemDtoForItemRequest> itemDtoForItemRequests = items.stream().map(itemMapperMapStruct::inItemDtoForItemRequest).toList();
        itemRequestDtoInConsoleWithItems.setItems(itemDtoForItemRequests);
        return itemRequestDtoInConsoleWithItems;
    }
}
