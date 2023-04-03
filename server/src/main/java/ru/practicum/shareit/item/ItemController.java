package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    public static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto createItem(@RequestHeader(USER_ID) Long userId,
                              @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на создание вещи");
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(USER_ID) Long userId, @PathVariable Long itemId,
                              @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на изменение вещи");
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemOwnerDto getItem(@RequestHeader(USER_ID) Long userId, @PathVariable Long itemId) {
        log.info(String.format("От пользователя %s получен запрос вещи с id %s", userId, itemId));
        return itemService.getItem(itemId, userId);
    }

    @GetMapping
    public List<ItemOwnerDto> getItemsOwners(@RequestHeader(USER_ID) Long userId,
                                             @RequestParam(name = "from", defaultValue = "0")
                                             Integer from,
                                             @RequestParam(name = "size", defaultValue = "20")
                                                 Integer size) {
        log.info(String.format("Пользователь %s запросил все свои вещи", userId));
        return itemService.getItemsOwners(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam("text") String text,
                                     @RequestParam(name = "from", defaultValue = "0")
                                     Integer from,
                                     @RequestParam(name = "size", defaultValue = "20")
                                         Integer size) {
        if (text.isBlank()) {
            log.info(String.format("Пользователь не заполнил параметр для поиска вещи"));
        } else {
            log.info(String.format("Пользователь ищет %s", text));
        }
        return itemService.searchItems(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(USER_ID) Long userId,
                                    @PathVariable Long itemId,
                                    @RequestBody CommentDto commentDto) {
        return itemService.createComment(commentDto, itemId, userId);
    }

}
