package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.EmailDuplicateException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplMockTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;
    @Mock
    private UserMapper userMapper;

    @Test
    void updateUserNotFoundTest() {
        UserDto userDto = new UserDto(99L, "test@test.ru", "Alex");
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(ResponseStatusException.class,
                () -> userServiceImpl.updateUser(userDto, userDto.getId()));
    }

    @Test
    void updateUserEmailDuplicateTest() {
        User user = new User(1L, "test@test.ru", "Alex");
        User user2 = new User(2L, "test2@test.ru", "Max");
        UserDto userUpdatedDto = new UserDto(1L, "test2@test.ru", "Alex");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.findAll()).thenReturn(List.of(user2));
        Assertions.assertThrows(EmailDuplicateException.class,
                () -> userServiceImpl.updateUser(userUpdatedDto, userUpdatedDto.getId()));
    }

    @Test
    void deleteUserNotFoundTest() {
        UserDto userDto = new UserDto(99L, "test@test.ru", "Alex");
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(ResponseStatusException.class,
                () -> userServiceImpl.deleteUser(userDto.getId()));
    }

    @Test
    void getUserNotFoundTest() {
        UserDto userDto = new UserDto(99L, "test@test.ru", "Alex");
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(ResponseStatusException.class,
                () -> userServiceImpl.getUser(userDto.getId()));
    }

    @Test
    void getUserTest() {
        User user = new User(1L, "test@test.ru", "Alex");
        UserDto userDto = new UserDto(1L, "test@test.ru", "Alex");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userDto);
        Assertions.assertEquals(userDto, userServiceImpl.getUser(user.getId()));
    }

}