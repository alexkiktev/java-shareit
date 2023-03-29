package ru.practicum.shareit.user.controller;

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
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    @Test
    void givenUserDto_whenSuccessful_CreateUserTest() throws Exception {
        UserDto userDto = new UserDto(null, "test@test.ru", "Alex");
        when(userService.createUser(any())).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }

    @Test
    void throwException_whenNameIsBlank_CreateUserTest() throws Exception {
        UserDto userDto = new UserDto(null, "test@test.ru", "");

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void throwException_whenEmailIsBlank_CreateUserTest() throws Exception {
        UserDto userDto = new UserDto(null, "", "Alex");

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void throwException_whenEmailIncorrectFormat_CreateUserTest() throws Exception {
        UserDto userDto = new UserDto(null, "testemail.ru", "Alex");

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenUserDto_whenSuccessful_UpdateUserTest() throws Exception {
        Long userId = 1L;
        UserDto userDto = new UserDto(userId, "new@test.ru", "Alex");
        when(userService.updateUser(userDto, userId)).thenReturn(userDto);

        mockMvc.perform(patch("/users/" + userId)
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())))
                .andExpect(jsonPath("$.name", is(userDto.getName())));

        verify(userService, times(1)).updateUser(userDto, userId);
    }

    @Test
    void throwException_whenEmailIncorrectFormat_UpdateUserTest() throws Exception {
        Long userId = 1L;
        UserDto userDto = new UserDto(userId, "testemail.ru", "Alex");
        when(userService.updateUser(userDto, userId)).thenReturn(userDto);

        mockMvc.perform(patch("/users/" + userId)
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void successful_deleteUserTest() throws Exception {
        Long userId = 1L;

        mockMvc.perform(delete("/users/" + userId))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void givenUserDto_whenSuccessful_GetUserTest() throws Exception {
        Long userId = 1L;
        UserDto userDto = new UserDto(userId, "test@test.ru", "Alex");
        when(userService.getUser(userId)).thenReturn(userDto);

        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())))
                .andExpect(jsonPath("$.name", is(userDto.getName())));

        verify(userService, times(1)).getUser(userId);
    }

    @Test
    void throwException_whenUserNotFound_getUserTest() throws Exception {
        Long userId = 1L;
        when(userService.getUser(userId)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenUserDtoList_whenSuccessful_GetAllUserTest() throws Exception {
        UserDto userDto = new UserDto(1L, "test@test.ru", "Alex");
        UserDto userDto2 = new UserDto(2L, "test@test.ru", "Alex");
        when(userService.getAllUsers()).thenReturn(List.of(userDto, userDto2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());

        verify(userService, times(1)).getAllUsers();
    }

}