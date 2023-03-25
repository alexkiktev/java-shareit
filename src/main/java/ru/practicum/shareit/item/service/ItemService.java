package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    ItemOwnerDto getItem(Long itemId, Long userId);

    List<ItemOwnerDto> getItemsOwners(Long userId);

    List<ItemDto> searchItems(String text);

    CommentDto createComment(CommentDto commentDto, Long itemId, Long userId);

}
