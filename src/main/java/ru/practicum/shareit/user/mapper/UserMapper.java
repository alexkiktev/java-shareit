package ru.practicum.shareit.user.mapper;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Service
public class UserMapper {

    public UserDto toUserDto(@NotNull User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }

    public User toUser(@NotNull UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getEmail(),
                userDto.getName()
        );
    }

}
