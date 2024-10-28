package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDtoFromConsole;
import ru.practicum.shareit.item.dto.CommentDtoInConsole;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithCommentAndDate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {
    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    private ItemDto itemDtoFrom;
    private ItemDto itemDtoIn;
    private ItemDtoWithCommentAndDate itemDtoWithCommentAndDate;
    private CommentDtoFromConsole commentDtoFromConsole;
    private CommentDtoInConsole commentDtoInConsole;
    long userId = 1;
    String text = "text";
    long itemId = 1;
    long requestId = 1;


    @BeforeEach
    void setUp() {
        itemDtoFrom = new ItemDto();
        itemDtoFrom.setName("Name");
        itemDtoFrom.setDescription("Description");
        itemDtoFrom.setAvailable(true);
        itemDtoIn = new ItemDto();
        itemDtoIn.setId(1L);
        itemDtoIn.setName("Name");
        itemDtoIn.setDescription("Description");
        itemDtoIn.setAvailable(true);
        itemDtoWithCommentAndDate = new ItemDtoWithCommentAndDate();

        itemDtoWithCommentAndDate.setId(1L);
        itemDtoWithCommentAndDate.setName("Name");
        itemDtoWithCommentAndDate.setDescription("Description");
        itemDtoWithCommentAndDate.setAvailable(true);
        itemDtoWithCommentAndDate.setLastBooking(LocalDateTime.of(2024, 10, 24, 16, 43, 22));
        itemDtoWithCommentAndDate.setNextBooking(LocalDateTime.of(2024, 10, 24, 17, 43, 22));

        commentDtoFromConsole = new CommentDtoFromConsole();
        commentDtoFromConsole.setText("Text");

        commentDtoInConsole = new CommentDtoInConsole();
        commentDtoInConsole.setId(1L);
    }

    @Test
    void testAddItem() throws Exception {
        when(itemService.addItemJpa(userId, itemDtoFrom)).thenReturn(itemDtoIn);
        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .content(mapper.writeValueAsString(itemDtoFrom))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":1,\"name\":\"Name\",\"description\":\"Description\",\"available\":true,\"requestId\":null}"))
                .andExpect(jsonPath("$.id", is(1)));
        verify(itemService, times(1)).addItemJpa(userId, itemDtoFrom);
    }

    @Test
    void testUpdateItem() throws Exception {
        when(itemService.updateItemJpa(userId, itemDtoFrom, itemId)).thenReturn(itemDtoIn);
        mvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .content(mapper.writeValueAsString(itemDtoFrom))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"Name\",\"description\":\"Description\",\"available\":true,\"requestId\":null}"))
                .andExpect(jsonPath("$.id", is(1)));
        verify(itemService, times(1)).updateItemJpa(userId, itemDtoFrom, itemId);
    }

    @Test
    void testGetItemById() throws Exception {
        when(itemService.getItemByIdJpa(userId, itemId)).thenReturn(itemDtoWithCommentAndDate);
        mvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"Name\",\"description\":\"Description\",\"available\":true,\"request\":null,\"comments\":null,\"lastBooking\":\"2024-10-24T16:43:22\",\"nextBooking\":\"2024-10-24T17:43:22\"}"))
                .andExpect(jsonPath("$.id", is(1)));
        verify(itemService, times(1)).getItemByIdJpa(userId, itemId);
    }

    @Test
    void testAddComment() throws Exception {
        commentDtoFromConsole.setItemId(itemId);
        commentDtoFromConsole.setUserId(userId);
        when(itemService.addComment(commentDtoFromConsole)).thenReturn(commentDtoInConsole);
        mvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .content(mapper.writeValueAsString(commentDtoFromConsole))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":1,\"text\":null,\"authorName\":null,\"itemId\":null,\"created\":null}"))
                .andExpect(jsonPath("$.id", is(1)));
        verify(itemService, times(1)).addComment(commentDtoFromConsole);
    }

    @Test
    void testGetItemsFromUsers() throws Exception {
        when(itemService.getItemsFromUsersJpa(userId)).thenReturn(List.of(itemDtoWithCommentAndDate));
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"name\":\"Name\",\"description\":\"Description\",\"available\":true,\"request\":null,\"comments\":null,\"lastBooking\":\"2024-10-24T16:43:22\",\"nextBooking\":\"2024-10-24T17:43:22\"}]"))
                .andExpect(jsonPath("$.[0].id", is(1)));
        verify(itemService, times(1)).getItemsFromUsersJpa(userId);
    }

    @Test
    void testSearch() throws Exception {
        when(itemService.searchJpa(text, userId)).thenReturn(List.of(itemDtoIn));
        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", userId)
                        .param("text", text)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"name\":\"Name\",\"description\":\"Description\",\"available\":true,\"requestId\":null}]"))
                .andExpect(jsonPath("$.[0].id", is(1)));
        verify(itemService, times(1)).searchJpa(text, userId);
    }

    @Test
    void testAddItemWithRequest() throws Exception {
        itemDtoFrom.setRequestId(requestId);
        itemDtoIn.setRequestId(requestId);
        System.out.println(mapper.writeValueAsString(itemDtoFrom));
        when(itemService.addItemJpa(userId, itemDtoFrom)).thenReturn(itemDtoIn);
        mvc.perform(post("/items/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId)
                        .content(mapper.writeValueAsString(itemDtoFrom))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":1,\"name\":\"Name\",\"description\":\"Description\",\"available\":true,\"requestId\":1}"))
                .andExpect(jsonPath("$.id", is(1)));
        verify(itemService, times(1)).addItemJpa(userId, itemDtoFrom);
    }
}