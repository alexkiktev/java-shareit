package ru.practicum.shareit.request;

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
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestItemInfoDto;
import ru.practicum.shareit.request.service.RequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RequestController.class)
@AutoConfigureMockMvc
class RequestControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private RequestService requestService;

    private Long userId;
    private Long requestId;
    private LocalDateTime created;
    private ItemDto itemDto;
    private List<ItemDto> listItemDto;
    private RequestDto requestDto;
    private RequestItemInfoDto requestItemInfoDto;

    @BeforeEach
    void setUp() {
        userId = 1L;
        requestId = 1L;
        created = LocalDateTime.now().plusHours(1);
        itemDto = new ItemDto(null, "Отвертка", "Крестовая отвертка", true, null);
        listItemDto = List.of(itemDto);
        requestDto = new RequestDto(requestId, "Описание", userId, created);
        requestItemInfoDto = new RequestItemInfoDto(requestId, "Описание", created, listItemDto);
    }


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