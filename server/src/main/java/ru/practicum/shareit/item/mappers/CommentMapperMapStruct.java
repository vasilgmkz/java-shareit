package ru.practicum.shareit.item.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.practicum.shareit.item.dto.CommentDtoFromConsole;
import ru.practicum.shareit.item.dto.CommentDtoInConsole;
import ru.practicum.shareit.item.model.Comment;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {CommentMapperMapStructUtil.class}, imports = {
        Instant.class, ZoneOffset.class, LocalDateTime.class
})
public interface CommentMapperMapStruct {
    @Mapping(target = "created", expression = "java(LocalDateTime.now().toInstant(ZoneOffset.UTC))")
    Comment inComment(CommentDtoFromConsole commentDtoFromConsole);

    @Mapping(target = "created", qualifiedByName = {"CommentMapperMapStructUtil", "fromInstant"}, source = "created")
    @Mapping(target = "authorName", expression = "java(comment.getAuthor().getName())")
    @Mapping(target = "itemId", expression = "java(comment.getItem().getId())")
    CommentDtoInConsole fromComment(Comment comment);

    @Mapping(target = "created", qualifiedByName = {"CommentMapperMapStructUtil", "fromInstant"}, source = "created")
    @Mapping(target = "authorName", expression = "java(comment.getAuthor().getName())")
    @Mapping(target = "itemId", expression = "java(comment.getItem().getId())")
    List<CommentDtoInConsole> fromComments(List<Comment> comments);
}

