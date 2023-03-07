package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Component
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        return userMapper.toUserDto(userStorage.createUser(userMapper.toUser(userDto)));
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long id) {
        return userMapper.toUserDto(userStorage.updateUser(userMapper.toUser(userDto), id));
    }

    @Override
    public void deleteUser(Long id) {
        userStorage.deleteUser(id);
    }

    @Override
    public UserDto getUser(Long id) {
        return userMapper.toUserDto(userStorage.getUser(id));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userStorage.getAllUsers().stream().map(userMapper::toUserDto).collect(Collectors.toList());
    }

}
