package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Component
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserServiceImpl userService;
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
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        List<Item> itemsFound = itemStorage.searchItems(text);
        return itemsFound.stream().map(itemMapper::toItemDto).collect(Collectors.toList());
    }

}
