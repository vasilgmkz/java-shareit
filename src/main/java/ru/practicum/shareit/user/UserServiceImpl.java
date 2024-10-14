package ru.practicum.shareit.user;

import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ConflictExceptions;
import ru.practicum.shareit.exceptions.InternalServerException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mappers.UserMapperMapStruct;
import ru.practicum.shareit.user.model.User;

@Data
@RequiredArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserMapperMapStruct userMapperMapStruct;
    private final UserRepositoryJpa userRepositoryJpa;

    @Override
    public UserDto updateUserJpa(long userId, UserDto userDto) {
        User userFromBase = userRepositoryJpa.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с " + userId + " не найден"));
        if (userRepositoryJpa.findByEmailContainingIgnoreCase(userDto.getEmail()) != null) {
            throw new ConflictExceptions("Пользователь с email " + userDto.getEmail() + " существует");
        }
        User userInBase = userMapperMapStruct.fromUserDto(userDto);
        if (userInBase.getName() != null) {
            userFromBase.setName(userInBase.getName());
        }
        if (userInBase.getEmail() != null) {
            userFromBase.setEmail(userInBase.getEmail());
        }
        return userMapperMapStruct.toUserDto(userRepositoryJpa.save(userFromBase));
    }

    @Override
    public UserDto getUserDtoByIdJpa(long userId) {
        return userMapperMapStruct.toUserDto(userRepositoryJpa.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с " + userId + " не найден")));
    }


    @Override
    public void deleteUserByIdJpa(long userId) {
        getUserDtoByIdJpa(userId);
        userRepositoryJpa.deleteById(userId);
    }


    @Override
    public UserDto addUserJpa(UserDto userDto) {
        User user = userMapperMapStruct.fromUserDto(userDto);
        Long id = userRepositoryJpa.save(user).getId();
        if (id == null) {
            throw new InternalServerException("Не удалось сохранить данные");
        }
        user.setId(id);
        return userMapperMapStruct.toUserDto(user);
    }
}

