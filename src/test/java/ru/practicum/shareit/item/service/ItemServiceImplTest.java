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
    void succesful_UserIsOwner_updateItemTest() {
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

}