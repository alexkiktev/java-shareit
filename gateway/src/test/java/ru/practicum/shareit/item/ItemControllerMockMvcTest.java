package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.ItemDto;

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
    private ItemClient itemClient;

    Long userId = 1L;
    Long itemId = 1L;
    ItemDto newItemDto = new ItemDto(null, "Отвертка", "Крестовая отвертка",
            true, null);

    @Test
    void throwException_whenNameIsBlank_createItemTest() throws Exception {
        newItemDto.setName("");

        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(newItemDto))
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void throwException_whenDescriptionIsBlank_createItemTest() throws Exception {
        newItemDto.setDescription("");

        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(newItemDto))
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void throwException_whenAvailableIsNull_createItemTest() throws Exception {
        newItemDto.setAvailable(null);

        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(newItemDto))
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void throwException_whenUserNotFound_getItemTest() throws Exception {
        Long controlUserId = 9999999999L;
        when(itemClient.getItem(any(), any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/items/" + itemId)
                        .header("X-Sharer-User-Id", controlUserId))
                .andExpect(status().isNotFound());
    }

}