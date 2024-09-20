package ru.practicum.shareit.user;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private Long count = 0L;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        count++;
        user.setId(count);
        users.put(count, user);
        return user;
    }

    @Override
    public Long validationEmail(User user) {
        return users.values().stream().filter(x -> x.getEmail().equals(user.getEmail())).count();
    }

    @Override
    public User updateUser(long userId, User user) {
        User userForUpdate = users.get(userId);
        if (user.getName() != null) {
            userForUpdate.setName(user.getName());
        }
        if (user.getEmail() != null) {
            userForUpdate.setEmail(user.getEmail());
        }
        users.put(userId, userForUpdate);
        return userForUpdate;
    }

    @Override
    public Optional<User> getUserById(long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public boolean deleteUserById(long userId) {
        return users.remove(userId) != null;
    }
}
