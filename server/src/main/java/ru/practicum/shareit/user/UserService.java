package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {

    void deleteUserByIdJpa(long userId);

    UserDto addUserJpa(UserDto userDto);

    UserDto getUserDtoByIdJpa(long userId);

    UserDto updateUserJpa(long userId, UserDto userDto);
}
