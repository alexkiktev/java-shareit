package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.utils.MarkerValidation;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@Slf4j
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemClient itemClient;
    public static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(USER_ID) Long userId,
                                             @Validated(MarkerValidation.OnCreate.class) @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на создание вещи");
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(USER_ID) Long userId, @PathVariable Long itemId,
                              @Validated(MarkerValidation.OnUpdate.class) @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на изменение вещи");
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader(USER_ID) Long userId, @PathVariable Long itemId) {
        log.info(String.format("От пользователя %s получен запрос вещи с id %s", userId, itemId));
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsOwners(@RequestHeader(USER_ID) Long userId,
                                             @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                             Integer from,
                                             @Positive @RequestParam(name = "size", defaultValue = "20")
                                                 Integer size) {
        log.info(String.format("Пользователь %s запросил все свои вещи", userId));
        return itemClient.getItemsOwners(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam("text") String text,
                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                     Integer from,
                                     @Positive @RequestParam(name = "size", defaultValue = "20")
                                         Integer size) {
        if (text.isBlank()) {
            log.info(String.format("Пользователь не заполнил параметр для поиска вещи"));
        } else {
            log.info(String.format("Пользователь ищет %s", text));
        }
        return itemClient.searchItems(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(USER_ID) Long userId,
                                    @PathVariable Long itemId,
                                    @Valid @RequestBody CommentDto commentDto) {
        return itemClient.createComment(userId, itemId, commentDto);
    }

}
