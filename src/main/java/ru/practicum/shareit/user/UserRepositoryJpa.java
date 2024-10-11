package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

@Repository
public interface UserRepositoryJpa extends JpaRepository<User, Long> {
    User findByEmailContainingIgnoreCase(String emailSearch);
}
