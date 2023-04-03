package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemService itemService;

    private Long userId;
    private Long itemId;
    private ItemDto newItemDto;
    private ItemDto updatedItemDto;
    private ItemOwnerDto itemOwnerDto;
    private ItemOwnerDto itemOwnerDto2;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        userId = 1L;
        itemId = 1L;
        newItemDto = new ItemDto(null, "Отвертка", "Крестовая отвертка",
                true, null);
        updatedItemDto = new ItemDto(itemId, "Отвертка", "Крестовая отвертка",
                true, null);
        itemOwnerDto = ItemOwnerDto.builder()
                .id(itemId)
                .name("Отвертка")
                .description("Крестовая отвертка")
                .available(true)
                .ownerId(userId)
                .lastBooking(null)
                .nextBooking(null)
                .comments(null)
                .build();
        itemOwnerDto2 = ItemOwnerDto.builder()
                .id(itemId)
                .name("Дрель")
                .description("Электроинструмент")
                .available(true)
                .ownerId(userId)
                .lastBooking(null)
                .nextBooking(null)
                .comments(null)
                .build();
        commentDto = new CommentDto(null, "Комментарий", "Имя автора", LocalDateTime.now());
    }

    @Test
    void givenItemDto_whenSuccessful_createItemTest() throws Exception {
        when(itemService.createItem(any(), any())).thenReturn(updatedItemDto);

        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(newItemDto))
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedItemDto)));
    }

    @Test
    void givenItemDto_whenSuccessful_updateItemTest() throws Exception {
        when(itemService.updateItem(any(), any(), any())).thenReturn(updatedItemDto);

        mockMvc.perform(patch("/items/" + itemId)
                        .content(objectMapper.writeValueAsString(newItemDto))
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedItemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(updatedItemDto.getName())))
                .andExpect(jsonPath("$.description", is(updatedItemDto.getDescription())));

        verify(itemService, times(1)).updateItem(userId, itemId, newItemDto);
    }

    @Test
    void givenItemOwnerDto_whenSuccessful_getItemTest() throws Exception {
        when(itemService.getItem(any(), any())).thenReturn(itemOwnerDto);

        mockMvc.perform(get("/items/" + itemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemOwnerDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemOwnerDto.getName())))
                .andExpect(jsonPath("$.description", is(itemOwnerDto.getDescription())));

        verify(itemService, times(1)).getItem(itemId, userId);
    }

    @Test
    void throwException_whenUserNotFound_getItemTest() throws Exception {
        Long controlUserId = 9999999999L;
        when(itemService.getItem(any(), any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/items/" + itemId)
                        .header("X-Sharer-User-Id", controlUserId))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenItemOwnerDtoList_whenSuccessful_getItemsOwnersTest() throws Exception {
        when(itemService.getItemsOwners(any(), any(), any())).thenReturn(List.of(itemOwnerDto, itemOwnerDto2));

        mockMvc.perform(get("/items?from=0&size=10")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(itemService, times(1)).getItemsOwners(userId, 0, 10);
    }

    @Test
    void givenItemDtoList_whenSuccessful_searchItemsTest() throws Exception {
        String text = "Отвертка";
        when(itemService.searchItems(any(), any(), any())).thenReturn(List.of(updatedItemDto));

        mockMvc.perform(get("/items/search?text=" + text)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(itemService, times(1)).searchItems(text, 0, 20);
    }

    @Test
    void givenCommentDto_whenSuccessful_CreateItemTest() throws Exception {
        when(itemService.createComment(any(), any(), any())).thenReturn(commentDto);

        mockMvc.perform(post("/items/" + itemId + "/comment")
                        .content(objectMapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(commentDto)));
    }

}