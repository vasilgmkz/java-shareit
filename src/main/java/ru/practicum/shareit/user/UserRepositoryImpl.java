package ru.practicum.shareit.user;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Data
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private Long count = 0L;
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    @Override
    public User addUser(User user) {
        count++;
        user.setId(count);
        emails.add(user.getEmail());
        users.put(count, user);
        return user;
    }

    @Override
    public boolean validationEmail(String userEmail) {
        return emails.contains(userEmail);
    }

    @Override
    public User updateUser(long userId, User user) {
        User userForUpdate = users.get(userId);
        if (user.getName() != null) {
            userForUpdate.setName(user.getName());
        }
        if (user.getEmail() != null) {
            emails.remove(userForUpdate.getEmail());
            emails.add(user.getEmail());
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
