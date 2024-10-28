package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.InternalServerException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDtoFromConsole;
import ru.practicum.shareit.item.dto.CommentDtoInConsole;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithCommentAndDate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplTest {

    private final ItemService itemService;

    ItemDto itemDtoFrom;
    CommentDtoFromConsole commentDtoFromConsole;
    long goodUserId = 1;
    long badUserId = 10;
    long goodItemId = 1;
    long badItemId = 10;

    @BeforeEach
    void setUp() {
        itemDtoFrom = new ItemDto();
        itemDtoFrom.setName("name_4");
        itemDtoFrom.setDescription("description_4");
        itemDtoFrom.setAvailable(true);
        commentDtoFromConsole = new CommentDtoFromConsole();

    }

    @Test
    @DisplayName("Создание вещи")
    void testAddItemJpa() {
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> itemService.addItemJpa(badUserId, itemDtoFrom));
        assertEquals(exception.getMessage(), "Пользователь с 10 не найден");
        ItemDto itemDtoIn = itemService.addItemJpa(goodUserId, itemDtoFrom);
        assertEquals(itemDtoIn.getId(), 4);
    }

    @Test
    @DisplayName("Обновление вещи")
    void testUpdateItemJpa() {
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> itemService.updateItemJpa(goodUserId, itemDtoFrom, badItemId));
        assertEquals(exception.getMessage(), "Вещь с 10 не найдена");
        NotFoundException exception1 = Assertions.assertThrows(NotFoundException.class, () -> itemService.updateItemJpa(goodUserId, itemDtoFrom, goodItemId + 1));
        assertEquals(exception1.getMessage(), "Вещь не пренадлежит пользователю с id 1");
        ItemDto itemDtoIn = itemService.updateItemJpa(goodUserId, itemDtoFrom, goodItemId);
        assertEquals(itemDtoIn.getId(), 1);
        assertEquals(itemDtoIn.getName(), "name_4");
        itemDtoFrom.setName(null);
        itemDtoFrom.setDescription(null);
        itemDtoFrom.setAvailable(null);
        ItemDto itemDtoIn1 = itemService.updateItemJpa(goodUserId, itemDtoFrom, goodItemId);
        assertEquals(itemDtoIn1.getId(), 1);
        assertEquals(itemDtoIn1.getName(), "name_4");
    }

    @Test
    @DisplayName("Получение вещи")
    void testGetItemByIdJpa() {
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> itemService.getItemByIdJpa(goodUserId, badItemId));
        assertEquals(exception.getMessage(), "Вещь с 10 не найдена");
        ItemDtoWithCommentAndDate itemDtoWithCommentAndDate = itemService.getItemByIdJpa(goodUserId, goodItemId);
        assertEquals(itemDtoWithCommentAndDate.getName(), "name_1");
        ItemDtoWithCommentAndDate itemDtoWithCommentAndDate1 = itemService.getItemByIdJpa(goodUserId, goodItemId + 1);
        assertEquals(itemDtoWithCommentAndDate1.getName(), "name_2");
    }

    @Test
    @DisplayName("Получение вещей пользователя")
    void testGetItemsFromUsersJpa() {
        List<ItemDtoWithCommentAndDate> list = itemService.getItemsFromUsersJpa(goodUserId);
        assertEquals(list.size(), 1);
        assertEquals(list.get(0).getName(), "name_1");
    }

    @Test
    @DisplayName("Поиск вещи")
    void testSearchJpa() {
        List<ItemDto> list = itemService.searchJpa("", goodUserId);
        assertEquals(list.size(), 0);
        List<ItemDto> list1 = itemService.searchJpa("description_3", goodUserId);
        assertEquals(list1.size(), 1);
        assertEquals(list1.get(0).getName(), "name_3");
    }

    @Test
    @DisplayName("Добавить комментарий")
    void testAddComment() {
        commentDtoFromConsole.setItemId(goodItemId);
        commentDtoFromConsole.setUserId(goodUserId + 1);
        commentDtoFromConsole.setText("Text");
        CommentDtoInConsole commentDtoInConsole = itemService.addComment(commentDtoFromConsole);
        assertEquals(commentDtoInConsole.getId(), 1);
        assertEquals(commentDtoInConsole.getText(), "Text");
        commentDtoFromConsole.setItemId(badItemId);
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> itemService.addComment(commentDtoFromConsole));
        assertEquals(exception.getMessage(), "Вещь с 10 не найдена");
        commentDtoFromConsole.setItemId(goodItemId);
        commentDtoFromConsole.setUserId(badUserId);
        NotFoundException exception1 = Assertions.assertThrows(NotFoundException.class, () -> itemService.addComment(commentDtoFromConsole));
        assertEquals(exception1.getMessage(), "Пользователь с 10 не найден");
        commentDtoFromConsole.setItemId(goodItemId);
        commentDtoFromConsole.setUserId(goodUserId);
        InternalServerException exception3 = Assertions.assertThrows(InternalServerException.class, () -> itemService.addComment(commentDtoFromConsole));
        assertEquals(exception3.getMessage(), "Ошибка валидации при добавлении комментария. Возможно пользователь не брал вещь в аренду");
    }
}