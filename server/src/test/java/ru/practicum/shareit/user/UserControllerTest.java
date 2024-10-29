package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    UserDto userDtoFrom;
    UserDto userDtoIn;
    long userId = 1;


    @BeforeEach
    void setUp() {
        userDtoFrom = new UserDto();
        userDtoFrom.setName("name");
        userDtoFrom.setEmail("123@email.ru");

        userDtoIn = new UserDto();
        userDtoIn.setId(1L);
        userDtoIn.setName("name");
        userDtoIn.setEmail("123@email.ru");
    }

    @Test
    void testAddUser() throws Exception {
        when(userService.addUserJpa(userDtoFrom)).thenReturn(userDtoIn);
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDtoFrom))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":1,\"name\":\"name\",\"email\":\"123@email.ru\"}"))
                .andExpect(jsonPath("$.id", is(1)));
        verify(userService, times(1)).addUserJpa(userDtoFrom);
    }

    @Test
    void testUpdateUser() throws Exception {
        when(userService.updateUserJpa(userId, userDtoFrom)).thenReturn(userDtoIn);
        mvc.perform(patch("/users/{userId}", userId)
                        .content(mapper.writeValueAsString(userDtoFrom))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"name\",\"email\":\"123@email.ru\"}"))
                .andExpect(jsonPath("$.id", is(1)));
        verify(userService, times(1)).updateUserJpa(userId, userDtoFrom);
    }

    @Test
    void testGetUserById() throws Exception {
        when(userService.getUserDtoByIdJpa(userId)).thenReturn(userDtoIn);
        mvc.perform(get("/users/{userId}", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"name\",\"email\":\"123@email.ru\"}"))
                .andExpect(jsonPath("$.id", is(1)));
        verify(userService, times(1)).getUserDtoByIdJpa(userId);
    }

    @Test
    void testDeleteUserById() throws Exception {
        mvc.perform(delete("/users/{userId}", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userService, times(1)).deleteUserByIdJpa(userId);
    }
}