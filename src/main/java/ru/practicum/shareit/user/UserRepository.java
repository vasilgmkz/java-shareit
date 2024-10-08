package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserRepository {
    User addUser(User user);

    Long validationEmail(User user);

    User updateUser(long userId, User user);

    Optional<User> getUserById(long userId);

    boolean deleteUserById(long userId);
}
