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

    @Query(value = "select it from Item as it " +
            "join it.request as re " +
            "join re.requestor as req " +
            "where req.id = :userId " +
            "ORDER BY re.created asc")
    List<Item> getItemRequestsUserId(@Param("userId") Long userId);

    @Query(value = "select it from Item as it " +
            "join it.request as re " +
            "where re.id = :requestId")
    List<Item> getItemRequestsId(@Param("requestId") Long requestId);
}