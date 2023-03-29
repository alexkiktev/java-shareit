package ru.practicum.shareit.request.controller;

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
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestItemInfoDto;
import ru.practicum.shareit.request.service.RequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RequestController.class)
@AutoConfigureMockMvc
class RequestControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private RequestService requestService;

    Long userId = 1L;
    Long requestId = 1L;
    LocalDateTime created = LocalDateTime.now().plusHours(1);
    ItemDto itemDto =
            new ItemDto(null, "Отвертка", "Крестовая отвертка", true, null);
    List<ItemDto> listItemDto = List.of(itemDto);
    RequestDto requestDto = new RequestDto(requestId, "Описание", userId, created);
    RequestItemInfoDto requestItemInfoDto =
            new RequestItemInfoDto(requestId, "Описание", created, listItemDto);

    @Test
    void givenRequestDto_whenSuccessful_createRequestTest() throws Exception {
        when(requestService.createRequest(userId, requestDto)).thenReturn(requestDto);

        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(requestDto)));

        verify(requestService, times(1)).createRequest(userId, requestDto);
    }

    @Test
    void throwException_whenDescriptionIsNull_createRequestTest() throws Exception {
        requestDto.setDescription(null);

        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenRequestItemInfoDtoList_whenSuccessful_getRequestsByUser() throws Exception {
        when(requestService.getRequestByUser(userId)).thenReturn(List.of(requestItemInfoDto));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(requestService, times(1)).getRequestByUser(userId);
    }

    @Test
    void throwException_whenUserNotFound_getRequestsByUser() throws Exception {
        Long controlUserId = 99999999L;
        when(requestService.getRequestByUser(controlUserId))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", controlUserId))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenRequestItemInfoDtoList_whenSuccessful_getAllRequests() throws Exception {
        Integer from = 0;
        Integer size = 20;
        when(requestService.getAllRequests(userId, from, size)).thenReturn(List.of(requestItemInfoDto));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(requestService, times(1)).getAllRequests(userId, from, size);
    }

    @Test
    void givenRequestItemInfoDto_whenSuccessful_getRequestById() throws Exception {
        when(requestService.getRequestById(userId, requestId)).thenReturn(requestItemInfoDto);

        mockMvc.perform(get("/requests/" + requestId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(requestService, times(1)).getRequestById(userId, requestId);
    }

    @Test
    void throwException_whenUserNotFound_getRequestById() throws Exception {
        Long controlUserId = 99999999L;
        when(requestService.getRequestById(controlUserId, requestId))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/requests/" + requestId)
                        .header("X-Sharer-User-Id", controlUserId))
                .andExpect(status().isNotFound());
    }

}