package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
@Repository
public class InMemoryItemStorage implements ItemStorage {

    private final HashMap<Long, Item> items;
    private Long id = 0L;

    @Override
    public Item createItem(Item item) {
        item.setId(++id);
        items.put(item.getId(), item);
        log.info("Создана вещь: " + item.getName());
        return item;
    }

    @Override
    public Item updateItem(Item item, Long userId) {
        if (items.containsKey(item.getId())) {
            Item updatedItem = items.get(item.getId());
            if (userId.equals(updatedItem.getOwner())) {
                if (item.getName() != null) {
                    updatedItem.setName(item.getName());
                }
                if (item.getDescription() != null)
                {
                    updatedItem.setDescription(item.getDescription());
                }
                if (item.getAvailable() != null) {
                    updatedItem.setAvailable(item.getAvailable());
                }
                log.info("Обновлена вещь: " + updatedItem.getName());
                return updatedItem;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Вещь id %s не принадлежит пользователю %s", item.getId(), userId));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Вещь id %s не найдена!", item.getId()));
        }
    }

    @Override
    public Item getItem(Long itemId) {
        if (items.containsKey(itemId)) {
            return items.get(itemId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Вещь id %s не найдена!", itemId));
        }
    }

    @Override
    public List<Item> getItemsOwners(Long userId) {
        List<Item> itemsOwners = items.values().stream()
                .filter(i -> Objects.equals(i.getOwner(), userId)).collect(Collectors.toList());
        return itemsOwners;
    }

    @Override
    public List<Item> searchItems(String text) {
        List<Item> itemsFound = items.values().stream()
                .filter(i -> i.getName().toLowerCase().contains(text.toLowerCase()) ||
                        i.getDescription().toLowerCase().contains(text.toLowerCase()) && i.getAvailable().equals(true))
                .collect(Collectors.toList());
        return itemsFound;
    }

}
