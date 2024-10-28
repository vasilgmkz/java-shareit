package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRequestRepositoryJpa extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByRequestorId(long requestor);

    @Query(value = "select ir from ItemRequest as ir " +
            "join ir.requestor as user " +
            "where user.id != :userId")
    List<ItemRequest> getItemRequestsAll(@Param("userId") long userId);
}
