package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.utils.MarkerValidation;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemService itemService;
    public static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto createItem(@RequestHeader(USER_ID) Long userId,
                              @Validated(MarkerValidation.OnCreate.class) @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на создание вещи");
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(USER_ID) Long userId, @PathVariable Long itemId,
                              @Validated(MarkerValidation.OnUpdate.class) @RequestBody ItemDto itemDto) {
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
                                             @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                             Integer from,
                                             @Positive @RequestParam(name = "size", defaultValue = "20")
                                                 Integer size) {
        log.info(String.format("Пользователь %s запросил все свои вещи", userId));
        return itemService.getItemsOwners(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam("text") String text,
                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                     Integer from,
                                     @Positive @RequestParam(name = "size", defaultValue = "20")
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
                                    @Valid @RequestBody CommentDto commentDto) {
        return itemService.createComment(commentDto, itemId, userId);
    }

}
