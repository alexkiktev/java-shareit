package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.client.RequestClient;
import ru.practicum.shareit.request.dto.RequestDto;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RequestController.class)
@AutoConfigureMockMvc
class RequestControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private RequestClient requestClient;

    Long userId = 1L;
    Long requestId = 1L;
    LocalDateTime created = LocalDateTime.now().plusHours(1);
    RequestDto requestDto = new RequestDto(requestId, "Описание", userId, created);

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
    void throwException_whenUserNotFound_getRequestsByUser() throws Exception {
        Long controlUserId = 99999999L;
        when(requestClient.getRequestsByUser(controlUserId))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", controlUserId))
                .andExpect(status().isNotFound());
    }

    @Test
    void throwException_whenUserNotFound_getRequestById() throws Exception {
        Long controlUserId = 99999999L;
        when(requestClient.getRequestById(controlUserId, requestId))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/requests/" + requestId)
                        .header("X-Sharer-User-Id", controlUserId))
                .andExpect(status().isNotFound());
    }

}