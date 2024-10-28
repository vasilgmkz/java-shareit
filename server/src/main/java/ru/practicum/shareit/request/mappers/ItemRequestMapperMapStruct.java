package ru.practicum.shareit.request.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDtoFromConsole;
import ru.practicum.shareit.request.dto.ItemRequestDtoInConsoleCreated;
import ru.practicum.shareit.request.dto.ItemRequestDtoInConsoleWithItems;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ItemRequestMapperMapStructUtil.class}, imports = {
        Instant.class, ZoneOffset.class, LocalDateTime.class, ArrayList.class
})
public interface ItemRequestMapperMapStruct {
    @Mapping(target = "created", expression = "java(LocalDateTime.now().toInstant(ZoneOffset.UTC))")
    @Mapping(target = "requestor", source = "user")
    ItemRequest inItemRequest(ItemRequestDtoFromConsole itemRequestDtoFromConsole, User user);

    @Mapping(target = "created", qualifiedByName = {"ItemRequestMapperMapStructUtil", "fromInstant"}, source = "created")
    @Mapping(target = "requestorId", expression = "java(itemRequest.getRequestor().getId())")
    ItemRequestDtoInConsoleCreated inItemRequestDtoInConsole(ItemRequest itemRequest);

    @Mapping(target = "requestorId", expression = "java(itemRequest.getRequestor().getId())")
    @Mapping(target = "created", qualifiedByName = {"ItemRequestMapperMapStructUtil", "fromInstant"}, source = "created")
    @Mapping(target = "items", expression = "java(new ArrayList<>())")
    ItemRequestDtoInConsoleWithItems inItemRequestDtoInConsoleWithItems(ItemRequest itemRequest);
}