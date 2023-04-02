package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.repository.ItemRepository;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class ItemServiceImplTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemServiceImpl itemServiceImpl;

    @Test
    @Transactional
    @Sql("classpath:cleanup.sql")
    @Sql("classpath:test_users.sql")
    void successful_createItemTest() {
        Long userId = 1L;
        ItemDto itemDto = new ItemDto(null, "Отвертка", "Большая отвертка",
                true, null);
        ItemDto controlItemDto = new ItemDto(1L, "Отвертка", "Большая отвертка",
                true, null);

        ItemDto savedItemDto = itemServiceImpl.createItem(userId, itemDto);

        Assertions.assertEquals(controlItemDto.getId(), savedItemDto.getId());
        Assertions.assertEquals(controlItemDto.getName(), savedItemDto.getName());
        Assertions.assertEquals(controlItemDto.getAvailable(), savedItemDto.getAvailable());
    }

    @Test
    @Transactional
    @Sql("classpath:cleanup.sql")
    @Sql("classpath:test_users.sql")
    void throwException_whenUserNotFound_createItemTest() {
        Long userId = 999999999L;
        ItemDto itemDto = new ItemDto(null, "Отвертка", "Большая отвертка",
                true, null);

        Assertions.assertThrows(RuntimeException.class, () -> itemServiceImpl.createItem(userId, itemDto));
    }

    @Test
    @Transactional
    @Sql("classpath:cleanup.sql")
    @Sql("classpath:test_users.sql")
    void successful_UserIsOwner_updateItemTest() {
        Long userId = 1L;
        ItemDto itemInputDto = new ItemDto(null, "Отвертка", "Большая отвертка",
                true, null);
        ItemDto itemForUpdateDto = new ItemDto(null, "Крестовая отвертка", "Очень большая отвертка",
                true, null);
        ItemDto controlItemDto = new ItemDto(1L, "Крестовая отвертка", "Очень большая отвертка",
                true, null);

        ItemDto createdItem = itemServiceImpl.createItem(userId, itemInputDto);

        Assertions.assertEquals(itemInputDto.getName(), createdItem.getName());
        Assertions.assertEquals(itemInputDto.getAvailable(), createdItem.getAvailable());

        ItemDto updatedItemDto = itemServiceImpl.updateItem(userId, createdItem.getId(), itemForUpdateDto);

        Assertions.assertEquals(controlItemDto.getId(), updatedItemDto.getId());
        Assertions.assertEquals(controlItemDto.getName(), updatedItemDto.getName());
        Assertions.assertEquals(controlItemDto.getAvailable(), updatedItemDto.getAvailable());
    }

    @Test
    @Transactional
    @Sql("classpath:cleanup.sql")
    @Sql("classpath:test_users.sql")
    void throwException_whenUserNotFound_updateItemTest() {
        Long userId = 999999999L;
        Long itemId = 1L;
        ItemDto itemDto = new ItemDto(null, "Отвертка", "Большая отвертка",
                true, null);

        Assertions.assertThrows(RuntimeException.class, () -> itemServiceImpl.updateItem(userId, itemId, itemDto));
    }

    @Test
    @Transactional
    @Sql("classpath:cleanup.sql")
    @Sql("classpath:test_users.sql")
    void throwException_whenItemNotFound_updateItemTest() {
        Long controlId = 100L;
        Long itemId = 1L;
        ItemDto itemDto = new ItemDto(null, "Отвертка", "Большая отвертка",
                true, null);

        Assertions.assertThrows(RuntimeException.class, () -> itemServiceImpl.updateItem(controlId, itemId, itemDto));
    }

    @Test
    @Transactional
    @Sql("classpath:cleanup.sql")
    @Sql("classpath:test_users.sql")
    void throwException_whenUserIsNotOwner_updateItemTest() {
        Long ownerId = 1L;
        Long userId = 15L;
        ItemDto itemInputDto = new ItemDto(null, "Отвертка", "Большая отвертка",
                true, null);
        ItemDto itemForUpdateDto = new ItemDto(null, "Крестовая отвертка", "Очень большая отвертка",
                true, null);

        ItemDto createdItem = itemServiceImpl.createItem(ownerId, itemInputDto);

        Assertions.assertThrows(ResponseStatusException.class, () -> itemServiceImpl.updateItem(userId,
                createdItem.getId(), itemForUpdateDto));
    }

    @Test
    @Transactional
    @Sql("classpath:cleanup.sql")
    @Sql("classpath:test_users.sql")
    @Sql("classpath:test_items.sql")
    void successful_getItemTest() {
        Long userId = 3L;
        Long itemId = 1L;
        ItemOwnerDto controlItemDto = ItemOwnerDto.builder()
                .id(itemId)
                .name("Отбойный молоток")
                .description("Электрический инструмент")
                .available(true)
                .ownerId(1L)
                .lastBooking(null)
                .nextBooking(null)
                .comments(null)
                .build();

        ItemOwnerDto getItemDto = itemServiceImpl.getItem(itemId, userId);

        Assertions.assertEquals(controlItemDto.getId(), getItemDto.getId());
        Assertions.assertEquals(controlItemDto.getAvailable(), getItemDto.getAvailable());
    }

    @Test
    @Transactional
    @Sql("classpath:cleanup.sql")
    @Sql("classpath:test_users.sql")
    @Sql("classpath:test_items.sql")
    void throwException_whenUserNotFound_getItemTest() {
        Long userId = 999999999L;
        Long itemId = 1L;

        Assertions.assertThrows(RuntimeException.class, () -> itemServiceImpl.getItem(itemId, userId));
    }

    @Test
    @Transactional
    @Sql("classpath:cleanup.sql")
    @Sql("classpath:test_users.sql")
    @Sql("classpath:test_items.sql")
    void throwException_whenItemNotFound_getItemTest() {
        Long userId = 1L;
        Long itemId = 99999999999L;

        Assertions.assertThrows(RuntimeException.class, () -> itemServiceImpl.getItem(itemId, userId));
    }

    @Test
    @Transactional
    @Sql("classpath:cleanup.sql")
    @Sql("classpath:test_users.sql")
    @Sql("classpath:test_items.sql")
    void throwException_whenUserNotFound_getItemsOwners() {
        Long userId = 999999999L;
        Integer from = 0;
        Integer size = 20;

        Assertions.assertThrows(RuntimeException.class, () -> itemServiceImpl.getItemsOwners(userId, from, size));
    }

    @Test
    @Transactional
    @Sql("classpath:cleanup.sql")
    @Sql("classpath:test_users.sql")
    @Sql("classpath:test_items.sql")
    void throwException_whenUserDoesNotHaveItems_getItemsOwners() {
        Long userId = 3L;
        Integer from = 0;
        Integer size = 20;

        Assertions.assertThrows(RuntimeException.class, () -> itemServiceImpl.getItemsOwners(userId, from, size));
    }

}