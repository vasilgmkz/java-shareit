package ru.practicum.shareit.item.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ItemMapperMapStructUtil.class}, imports = {
        Instant.class
})
public interface ItemMapperMapStruct {
    @Mapping(target = "request", ignore = true)
    @Mapping(target = "id", ignore = true)
    Item fromItemDto(ItemDto itemDto);

    @Mapping(target = "request", qualifiedByName = {"ItemMapperMapStructUtil", "requestConvertor"}, source = "request")
    ItemDto toItemDto(Item item);

    @Mapping(target = "request", qualifiedByName = {"ItemMapperMapStructUtil", "requestConvertor"}, source = "request")
    List<ItemDto> toItemDtoList(List<Item> itemList);

    @Mapping(target = "request", qualifiedByName = {"ItemMapperMapStructUtil", "requestConvertor"}, source = "request")
    @Mapping(target = "lastBooking", qualifiedByName = {"ItemMapperMapStructUtil", "getLastBooking"}, source = "id")
    @Mapping(target = "nearestBooking", qualifiedByName = {"ItemMapperMapStructUtil", "getNearestBooking"}, source = "id")
    ItemDtoWithDate toItemDtoWithDate(Item item);

    @Mapping(target = "request", qualifiedByName = {"ItemMapperMapStructUtil", "requestConvertor"}, source = "request")
    @Mapping(target = "lastBooking", qualifiedByName = {"ItemMapperMapStructUtil", "getLastBooking"}, source = "id")
    @Mapping(target = "nearestBooking", qualifiedByName = {"ItemMapperMapStructUtil", "getNearestBooking"}, source = "id")
    List<ItemDtoWithDate> toItemsDtoWithDate(List<Item> itemList);

    @Mapping(target = "lastBooking", qualifiedByName = {"ItemMapperMapStructUtil", "getLastBooking"}, source = "id")
    @Mapping(target = "nextBooking", qualifiedByName = {"ItemMapperMapStructUtil", "getNearestBooking"}, source = "id")
    @Mapping(target = "request", qualifiedByName = {"ItemMapperMapStructUtil", "requestConvertor"}, source = "request")
    @Mapping(target = "comments", qualifiedByName = {"ItemMapperMapStructUtil", "getCommentsForItem"}, source = "id")
    ItemDtoWithCommentAndDate toItemDtoWithCommentAndDate(Item item);

    @Mapping(target = "lastBooking", qualifiedByName = {"ItemMapperMapStructUtil", "getLastBooking"}, source = "id")
    @Mapping(target = "nextBooking", qualifiedByName = {"ItemMapperMapStructUtil", "getNearestBooking"}, source = "id")
    @Mapping(target = "request", qualifiedByName = {"ItemMapperMapStructUtil", "requestConvertor"}, source = "request")
    @Mapping(target = "comments", qualifiedByName = {"ItemMapperMapStructUtil", "getCommentsForItem"}, source = "id")
    List<ItemDtoWithCommentAndDate> toItemsDtoWithCommentAndDate(List<Item> items);
}

