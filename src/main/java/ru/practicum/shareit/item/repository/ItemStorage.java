package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    Item createItem(Item item);

    Item updateItem(Item item, Long userId);

    Item getItem(Long itemId);

    List<Item> getItemsOwners(Long userId);

    List<Item> searchItems(String text);

}
