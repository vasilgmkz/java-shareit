package ru.practicum.shareit.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.ConflictExceptions;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {
    private final UserService userService;

    UserDto userDtoFrom;
    long userId = 1;

    @BeforeEach
    void setUp() {
        userDtoFrom = new UserDto();
        userDtoFrom.setName("name_5");
        userDtoFrom.setEmail("email_5@email.com");
    }

    @Test
    @DisplayName("Создание пользователя")
    void testAddUserJpa() {
        UserDto userDtoIn = userService.addUserJpa(userDtoFrom);
        assertEquals(userDtoIn.getId(), 5);
    }

    @Test
    @DisplayName("Обновление пользователя")
    void testUpdateUserJpa() {
        UserDto userDtoIn = userService.updateUserJpa(userId, userDtoFrom);
        assertEquals(userDtoIn.getId(), 1);
        assertEquals(userDtoIn.getName(), "name_5");
        userId = 5;
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> userService.updateUserJpa(userId, userDtoIn));
        assertEquals(exception.getMessage(), "Пользователь с 5 не найден");
        userId = 1;
        userDtoFrom.setEmail("email_1@email.com");
        ConflictExceptions exception1 = Assertions.assertThrows(ConflictExceptions.class, () -> userService.updateUserJpa(userId, userDtoIn));
        assertEquals(exception1.getMessage(), "Пользователь с email email_5@email.com существует");
        userDtoFrom.setEmail(null);
        UserDto userDtoIn1 = userService.updateUserJpa(userId, userDtoFrom);
        assertEquals(userDtoIn1.getEmail(), "email_5@email.com");
        userId = 2;
        userDtoFrom.setName(null);
        UserDto userDtoIn2 = userService.updateUserJpa(userId, userDtoFrom);
        assertEquals(userDtoIn2.getName(), "name_2");
    }

    @Test
    @DisplayName("Получение пользователя")
    void testGetUserDtoByIdJpa() {
        UserDto userDtoIn = userService.getUserDtoByIdJpa(userId);
        assertEquals(userDtoIn.getId(), 1);
        assertEquals(userDtoIn.getName(), "name_1");
    }

    @Test
    @DisplayName("Получение пользователя, несуществующий id")
    void testGetUserDtoByIdJpaBadId() {
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> userService.getUserDtoByIdJpa(5));
        assertEquals(exception.getMessage(), "Пользователь с 5 не найден");
    }

    @Test
    @DisplayName("Удаление пользователя")
    void testDeleteUserByIdJpa() {
        userService.deleteUserByIdJpa(userId);
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> userService.getUserDtoByIdJpa(userId));
        assertEquals(exception.getMessage(), "Пользователь с 1 не найден");
    }
}
