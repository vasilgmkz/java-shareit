package ru.practicum.shareit.request;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDtoFromConsole;
import ru.practicum.shareit.request.dto.ItemRequestDtoInConsoleCreated;
import ru.practicum.shareit.request.dto.ItemRequestDtoInConsoleWithItems;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceImplTest {

    private final ItemRequestService itemRequestService;

    ItemRequestDtoFromConsole itemRequestDtoFromConsole;

    @BeforeEach
    void setUp() {
        itemRequestDtoFromConsole = new ItemRequestDtoFromConsole();
        itemRequestDtoFromConsole.setDescription("Description");
    }

    @Test
    @DisplayName("Создание отзыва")
    void testAddItemRequestJpa() {
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> itemRequestService.addItemRequestJpa(itemRequestDtoFromConsole, 10));
        assertEquals(exception.getMessage(), "Пользователь с 10 не найден");
        ItemRequestDtoInConsoleCreated itemRequestDtoInConsoleCreated = itemRequestService.addItemRequestJpa(itemRequestDtoFromConsole, 1);
        assertEquals(itemRequestDtoInConsoleCreated.getRequestorId(), 1);
    }

    @Test
    @DisplayName("Получение отзывов пользователя")
    void testGetItemRequestsUserId() {
        List<ItemRequestDtoInConsoleWithItems> list = itemRequestService.getItemRequestsUserId(1);
        assertEquals(list.size(), 2);
        assertEquals(list.get(0).getDescription(), "description_4");
    }

    @Test
    @DisplayName("Получение отзывов пользователя")
    void testGetItemRequestsAll() {
        List<ItemRequestDtoInConsoleCreated> list = itemRequestService.getItemRequestsAll(1);
        assertEquals(list.size(), 2);
    }

    @Test
    @DisplayName("Получение отзыва по id")
    void testGetItemRequestId() {
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> itemRequestService.getItemRequestId(5));
        assertEquals(exception.getMessage(), "Запрос с 5 не найден");
        ItemRequestDtoInConsoleWithItems itemRequestDtoInConsoleWithItems = itemRequestService.getItemRequestId(1);
        assertEquals(itemRequestDtoInConsoleWithItems.getDescription(), "description_1");
    }
}
