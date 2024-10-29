package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDtoFromConsole;
import ru.practicum.shareit.request.dto.ItemRequestDtoInConsoleCreated;
import ru.practicum.shareit.request.dto.ItemRequestDtoInConsoleWithItems;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {
    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    long userId = 1;
    long requestId = 1;

    ItemRequestDtoFromConsole itemRequestDtoFromConsole;
    ItemRequestDtoInConsoleCreated itemRequestDtoInConsoleCreated;
    ItemRequestDtoInConsoleWithItems itemRequestDtoInConsoleWithItems;

    @BeforeEach
    void setUp() {
        itemRequestDtoFromConsole = new ItemRequestDtoFromConsole();
        itemRequestDtoFromConsole.setDescription("Description");

        itemRequestDtoInConsoleCreated = new ItemRequestDtoInConsoleCreated();
        itemRequestDtoInConsoleCreated.setId(1L);
        itemRequestDtoInConsoleCreated.setDescription("Description");

        itemRequestDtoInConsoleWithItems = new ItemRequestDtoInConsoleWithItems();
        itemRequestDtoInConsoleWithItems.setId(1L);
        itemRequestDtoInConsoleWithItems.setDescription("Description");
    }

    @Test
    void testAddItemRequestJpa() throws Exception {
        when(itemRequestService.addItemRequestJpa(itemRequestDtoFromConsole, userId)).thenReturn(itemRequestDtoInConsoleCreated);
        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .content(mapper.writeValueAsString(itemRequestDtoFromConsole))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":1,\"description\":\"Description\",\"requestorId\":null,\"created\":null}"))
                .andExpect(jsonPath("$.id", is(1)));
        verify(itemRequestService, times(1)).addItemRequestJpa(itemRequestDtoFromConsole, userId);
    }

    @Test
    void testGetItemRequestsUserId() throws Exception {
        when(itemRequestService.getItemRequestsUserId(userId)).thenReturn(List.of(itemRequestDtoInConsoleWithItems));
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"description\":\"Description\",\"requestorId\":null,\"created\":null,\"items\":null}]"))
                .andExpect(jsonPath("$.[0].id", is(1)));
        verify(itemRequestService, times(1)).getItemRequestsUserId(userId);
    }

    @Test
    void testGetItemRequestsAll() throws Exception {
        when(itemRequestService.getItemRequestsAll(userId)).thenReturn(List.of(itemRequestDtoInConsoleCreated));
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"description\":\"Description\",\"requestorId\":null,\"created\":null}]"))
                .andExpect(jsonPath("$.[0].id", is(1)));
        verify(itemRequestService, times(1)).getItemRequestsAll(userId);
    }

    @Test
    void testGetItemRequestId() throws Exception {
        when(itemRequestService.getItemRequestId(requestId)).thenReturn(itemRequestDtoInConsoleWithItems);
        mvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"description\":\"Description\",\"requestorId\":null,\"created\":null,\"items\":null}"))
                .andExpect(jsonPath("$.id", is(1)));
        verify(itemRequestService, times(1)).getItemRequestId(requestId);
    }
}