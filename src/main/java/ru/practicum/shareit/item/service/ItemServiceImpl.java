package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserService userService;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        userService.getUser(userId);
        return itemMapper.toItemDto(itemStorage.createItem(itemMapper.toItemNew(itemDto, userId)));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        userService.getUser(userId);
        itemDto.setId(itemId);
        return itemMapper.toItemDto(itemStorage.updateItem(itemMapper.toItemUpdate(itemDto), userId));
    }

    @Override
    public ItemDto getItem(Long itemId) {
        return itemMapper.toItemDto(itemStorage.getItem(itemId));
    }

    @Override
    public List<ItemDto> getItemsOwners(Long userId) {
        List<Item> itemsOwners = itemStorage.getItemsOwners(userId);
        if (itemsOwners.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("У пользователя %s нет зарегистрированных вещей", userId));
        }
        return itemsOwners.stream().map(itemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (StringUtils.isBlank(text)) {
            return new ArrayList<>();
        }
        List<Item> itemsFound = itemStorage.searchItems(text);
        return itemsFound.stream().map(itemMapper::toItemDto).collect(Collectors.toList());
    }

}
