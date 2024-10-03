package ru.practicum.shareit.item.mappers;


import org.mapstruct.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemMapperMapStruct {
    @Mapping(target = "request", ignore = true)
    @Mapping(target = "id", ignore = true)
    Item fromItemDto(ItemDto itemDto);

    @Mapping(target = "request", qualifiedByName = "requestConvertor", source = "request")
    ItemDto toItemDto (Item item);

    @Named("requestConvertor")
    default Long requestConvertor(ItemRequest request) {
        return request != null ? request.getId() : null;
    }
}




