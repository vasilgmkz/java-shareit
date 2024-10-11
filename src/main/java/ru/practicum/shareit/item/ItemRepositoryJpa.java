package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepositoryJpa extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(long owner);

    @Query(value = "select it from Item as it where it.available = true and (lower(it.name) like lower(concat('%',:search,'%')) or lower(it.description) like lower(concat('%',:search,'%')))")
    List<Item> searchJpa(@Param("search") String search);
}