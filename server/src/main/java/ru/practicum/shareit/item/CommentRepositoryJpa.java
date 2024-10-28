package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface CommentRepositoryJpa extends JpaRepository<Comment, Long> {

    @Query(value = "select cm from Comment as cm " +
            "join cm.item as it " +
            "where it.id = :itemId " +
            "ORDER BY cm.created desc")
    List<Comment> getCommentsForItem(@Param("itemId") Long itemId);
}
