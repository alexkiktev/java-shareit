package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplMockTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserService userService;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private ItemServiceImpl itemServiceImpl;

    Long userId;
    Item item;
    ItemDto itemDto;

    @BeforeEach
    void setUp() {
        userId = 1L;
        item = new Item(1L, "Дрель", "Электроинструмент", true, 2L, null);
        itemDto = new ItemDto(1L, "Дрель", "Электроинструмент", true, null);
    }

    @Test
    void updateItemNotFoundTest() {
        when(itemRepository.findById(itemDto.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(ResponseStatusException.class,
                () -> itemServiceImpl.updateItem(userId, itemDto.getId(), itemDto));
    }

    @Test
    void updateItemNotOwnerTest() {
        when(itemRepository.findById(itemDto.getId())).thenReturn(Optional.ofNullable(item));
        Assertions.assertThrows(ResponseStatusException.class,
                () -> itemServiceImpl.updateItem(userId, itemDto.getId(), itemDto));
    }

    @Test
    void getItemNotFoundTest() {
        when(itemRepository.findById(itemDto.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(ResponseStatusException.class,
                () -> itemServiceImpl.updateItem(userId, itemDto.getId(), itemDto));
    }

    @Test
    void getItemNotOwnerTest() {
        List<CommentDto> comment = new ArrayList<>();
        ItemOwnerDto itemOwnerDto = ItemOwnerDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwner())
                .comments(comment)
                .build();
        when(itemRepository.findById(itemDto.getId())).thenReturn(Optional.ofNullable(item));
        when(itemMapper.toItemOwnerDto(item)).thenReturn(itemOwnerDto);
        Assertions.assertEquals(itemOwnerDto, itemServiceImpl.getItem(item.getId(), userId));
    }

    @Test
    void getItemsOwners() {
    }

    @Test
    void searchItems() {
    }

    @Test
    void createComment() {
    }
}