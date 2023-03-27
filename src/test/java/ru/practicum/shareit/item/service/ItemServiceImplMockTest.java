package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.exception.CommentValidationException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplMockTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private UserServiceImpl userServiceImpl;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookingRepository bookingRepository;
    @InjectMocks
    private ItemServiceImpl itemServiceImpl;

    @Test
    void givenItemDto_whenSaveItem_thenReturnItemDtoTest() {
        Long userId = 5L;
        Item requiredItem = new Item(1L, "Дрель", "Электроинструмент",
                true, 5L, null);
        ItemDto requiredItemDto = new ItemDto(1L, "Дрель", "Электроинструмент",
                true, null);

        when(itemMapper.toItemNew(requiredItemDto, userId)).thenReturn(requiredItem);
        when(itemRepository.save(requiredItem)).thenReturn(requiredItem);
        when(itemMapper.toItemDto(requiredItem)).thenReturn(requiredItemDto);

        ItemDto controlItemDto = itemServiceImpl.createItem(userId, requiredItemDto);

        Assertions.assertEquals(controlItemDto, requiredItemDto);
        verify(itemRepository, times(1)).save(requiredItem);
    }

    @Test
    void throwException_whenItemNotFound_updateItemTest() {
        Long userId = 1L;
        ItemDto itemDto = new ItemDto(1L, "Дрель", "Электроинструмент", true, null);

        when(itemRepository.findById(itemDto.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class,
                () -> itemServiceImpl.updateItem(userId, itemDto.getId(), itemDto));
    }

    @Test
    void throwException_whenItemNotBelongUser_updateItemNTest() {
        Long userId = 1L;
        Item item = new Item(1L, "Дрель", "Электроинструмент", true, 2L, null);
        ItemDto itemDto = new ItemDto(1L, "Дрель", "Электроинструмент", true, null);

        when(itemRepository.findById(itemDto.getId())).thenReturn(Optional.ofNullable(item));

        Assertions.assertThrows(ResponseStatusException.class,
                () -> itemServiceImpl.updateItem(userId, itemDto.getId(), itemDto));
    }

    @Test
    void throwException_whenItemNotFound_getItemTest() {
        Long userId = 1L;
        Item item = new Item(1L, "Дрель", "Электроинструмент", true, 2L, null);
        ItemDto itemDto = new ItemDto(1L, "Дрель", "Электроинструмент", true, null);

        when(itemRepository.findById(itemDto.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class,
                () -> itemServiceImpl.getItem(userId, itemDto.getId()));
    }

    @Test
    void givenItemOwnerDto_whenItemNotBelongUser_getItemTest() {
        Long userId = 1L;
        Item item = new Item(1L, "Дрель", "Электроинструмент", true, 2L, null);
        ItemDto itemDto = new ItemDto(1L, "Дрель", "Электроинструмент", true, null);
        List<CommentDto> comment = new ArrayList<>();
        ItemOwnerDto itemOwnerDto = ItemOwnerDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwner())
                .comments(comment)
                .build();

        when(itemRepository.findById(itemDto.getId())).thenReturn(Optional.of(item));
        when(itemMapper.toItemOwnerDto(item)).thenReturn(itemOwnerDto);

        Assertions.assertEquals(itemOwnerDto, itemServiceImpl.getItem(item.getId(), userId));
    }

    @Test
    void givenItemOwnerDto_whenItemOwnerUser_getItemTest() {
        Long userId = 1L;
        Item item = new Item(1L, "Дрель", "Электроинструмент", true, 1L, null);
        ItemDto itemDto = new ItemDto(1L, "Дрель", "Электроинструмент", true, null);
        List<CommentDto> comment = new ArrayList<>();
        ItemOwnerDto itemOwnerDto = ItemOwnerDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwner())
                .comments(comment)
                .build();
        List<Booking> bookings = new ArrayList<>();

        when(itemRepository.findById(itemDto.getId())).thenReturn(Optional.of(item));
        when(itemMapper.toItemOwnerDto(item)).thenReturn(itemOwnerDto);
        when(bookingRepository.findBookingsByItemIdAndStatus(itemOwnerDto.getId(), Status.APPROVED))
                .thenReturn(bookings);

        Assertions.assertEquals(itemOwnerDto, itemServiceImpl.getItem(item.getId(), userId));
    }

    @Test
    void throwException_whenUserDoesNotHaveItems_getItemsOwnersTest() {
        Long userId = 1L;
        Pageable pageParams = PageRequest.of(0 / 20, 20);
        Page<Item> pageItem = Page.empty();

        when(itemRepository.findItemsByOwner(userId, pageParams)).thenReturn(pageItem);

        Assertions.assertThrows(ResponseStatusException.class,
                () -> itemServiceImpl.getItemsOwners(userId, 0, 20));
    }

    @Test
    void givenEmptyList_whenBlankText_searchItemsTest() {
        List<ItemDto> emptyItemIdoList = new ArrayList<>();

        Assertions.assertEquals(emptyItemIdoList, itemServiceImpl.searchItems("", 0, 20));
    }

    @Test
    void throwException_whenUserDoesNotHaveCompletedBookings_createCommentTest() {
        CommentDto commentDto = new CommentDto();
        Long response = 0L;

        when(bookingRepository.findBookingsByItemIdAndBookerIdAndComplete(anyLong(), anyLong())).thenReturn(response);

        Assertions.assertThrows(CommentValidationException.class,
                () -> itemServiceImpl.createComment(commentDto, anyLong(), anyLong()));
    }
}