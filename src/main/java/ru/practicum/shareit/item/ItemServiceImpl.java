package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ConflictExceptions;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.mappers.ItemMapperMapStruct;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemMapperMapStruct itemMapperMapStruct;


    @Override
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        Item item = itemMapperMapStruct.fromItemDto(itemDto);
        User user = userRepository.getUserById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        return itemMapperMapStruct.toItemDto(itemRepository.addItem(user, item));
    }

    @Override
    public ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId) {
        userRepository.getUserById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        Item item = itemRepository.getItemById(itemId).orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена"));
        if (!item.getOwner().getId().equals(userId)) {
            throw new ConflictExceptions("Вещь не пренадлежит пользователю с id " + userId);
        }
        return itemMapperMapStruct.toItemDto(itemRepository.updateItem(item, itemDto));
    }

    @Override
    public ItemDto getItemById(Long userId, Long itemId) {
        userRepository.getUserById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        return itemMapperMapStruct.toItemDto(itemRepository.getItemById(itemId).orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена")));
    }

    @Override
    public List<ItemDto> getItemsFromUsers(Long userId) {
        userRepository.getUserById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        return itemRepository.getItemsFromUsers(userId);
    }

    @Override
    public List<ItemDto> search(String search, Long userId) {
        userRepository.getUserById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        if (search.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.search(search);
    }
}
