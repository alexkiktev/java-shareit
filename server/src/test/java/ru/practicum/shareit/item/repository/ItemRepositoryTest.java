package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.model.Item;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    @Sql("classpath:cleanup.sql")
    @Sql("classpath:test_users.sql")
    @Sql("classpath:test_items.sql")
    void successful_findItemsByOwner() {
        Long userId = 1L;
        Pageable pageParams = PageRequest.of(0 / 20, 20);
        Page<Item> items = itemRepository.findItemsByOwner(userId, pageParams);

        Assertions.assertTrue(items.getContent().size() > 0);
    }

    @Test
    @Sql("classpath:cleanup.sql")
    @Sql("classpath:test_users.sql")
    @Sql("classpath:test_items.sql")
    void givenEmptyPages_whenUserDoesNotHaveItems_findItemsByOwner() {
        Long userId = 9999999999L;
        Pageable pageParams = PageRequest.of(0 / 20, 20);
        Page<Item> items = itemRepository.findItemsByOwner(userId, pageParams);

        Assertions.assertTrue(items.getContent().isEmpty());
    }

}