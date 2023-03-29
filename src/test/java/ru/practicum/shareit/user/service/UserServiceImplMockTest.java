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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplMockTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    void givenUserDto_whenSaveUser_thenReturnUserDtoTest() {
        User requiredUser = new User(1L, "a@a.ru", "Alex");
        UserDto requiredUserDto = new UserDto(1L, "a@a.ru", "Alex");
        when(userMapper.toUser(requiredUserDto)).thenReturn(requiredUser);
        when(userRepository.save(requiredUser)).thenReturn(requiredUser);
        when(userMapper.toUserDto(requiredUser)).thenReturn(requiredUserDto);

        UserDto controlUserDto = userServiceImpl.createUser(requiredUserDto);

        Assertions.assertEquals(controlUserDto, requiredUserDto);
        verify(userRepository, times(1)).save(requiredUser);
    }

    @Test
    void givenUserDto_whenUpdateUser_thenReturnUserDtoTest() {
        User requiredUser = new User(1L, "a@a.ru", "Alex");
        UserDto requiredUserDto = new UserDto(1L, "a@a.ru", "Alex");
        when(userRepository.findById(requiredUserDto.getId())).thenReturn(Optional.of(requiredUser));
        when(userRepository.save(requiredUser)).thenReturn(requiredUser);
        when(userMapper.toUserDto(requiredUser)).thenReturn(requiredUserDto);

        UserDto controlUserDto = userServiceImpl.updateUser(requiredUserDto, requiredUserDto.getId());

        Assertions.assertEquals(controlUserDto, requiredUserDto);
        verify(userRepository, times(1)).save(requiredUser);
    }

    @Test
    void throwException_whenUserNotFound_updateUserTest() {
        UserDto userDto = new UserDto(99L, "test@test.ru", "Alex");
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class,
                () -> userServiceImpl.updateUser(userDto, userDto.getId()));
    }

    @Test
    void throwException_whenEmailDuplicate_updateUserTest() {
        User user = new User(1L, "test@test.ru", "Alex");
        User user2 = new User(2L, "test2@test.ru", "Max");
        UserDto userUpdatedDto = new UserDto(1L, "test2@test.ru", "Alex");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.findAll()).thenReturn(List.of(user2));

        Assertions.assertThrows(EmailDuplicateException.class,
                () -> userServiceImpl.updateUser(userUpdatedDto, userUpdatedDto.getId()));
    }

    @Test
    void throwException_whenUserNotFound_deleteUserTest() {
        UserDto userDto = new UserDto(99L, "test@test.ru", "Alex");
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class,
                () -> userServiceImpl.deleteUser(userDto.getId()));
    }

    @Test
    void successful_whenDeleteUserTest() {
        User requiredUser = new User(1L, "a@a.ru", "Alex");
        when(userRepository.findById(requiredUser.getId())).thenReturn(Optional.of(requiredUser));

        userServiceImpl.deleteUser(requiredUser.getId());

        verify(userRepository, times(1)).delete(requiredUser);
    }

    @Test
    void throwException_whenUserNotFound_getUserTest() {
        UserDto userDto = new UserDto(99L, "test@test.ru", "Alex");
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class,
                () -> userServiceImpl.getUser(userDto.getId()));
    }

    @Test
    void givenUserDto_whenGetUserTest() {
        User requiredUser = new User(1L, "a@a.ru", "Alex");
        UserDto requiredUserDto = new UserDto(1L, "a@a.ru", "Alex");
        when(userRepository.findById(requiredUserDto.getId())).thenReturn(Optional.of(requiredUser));
        when(userMapper.toUserDto(requiredUser)).thenReturn(requiredUserDto);

        UserDto controlUserDto = userServiceImpl.getUser(requiredUserDto.getId());

        Assertions.assertNotNull(controlUserDto);
        Assertions.assertEquals(controlUserDto, requiredUserDto);
        verify(userRepository, times(1)).findById(requiredUserDto.getId());
    }

    @Test
    void givenListUserDto_whenGetAllUsersTest() {
        User requiredUser = new User(1L, "a@a.ru", "Alex");
        UserDto requiredUserDto = new UserDto(1L, "a@a.ru", "Alex");
        List<User> listUser = new ArrayList<>(List.of(requiredUser));
        List<UserDto> listUserDto = new ArrayList<>(List.of(requiredUserDto));
        when(userRepository.findAll()).thenReturn(listUser);
        when(userMapper.toUserDto(requiredUser)).thenReturn(requiredUserDto);

        List<UserDto> controlListUserDto = userServiceImpl.getAllUsers();

        Assertions.assertNotNull(controlListUserDto);
        Assertions.assertEquals(controlListUserDto, listUserDto);
        verify(userRepository, times(1)).findAll();
    }

}