package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.EmailDuplicateException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public UserDto createUser(UserDto userDto) {
        return userMapper.toUserDto(userRepository.save(userMapper.toUser(userDto)));
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public UserDto updateUser(UserDto userDto, Long id) {
        User updatedUser = userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь id " + id + "не найден!"));
        Optional.ofNullable(userDto.getName()).ifPresent(updatedUser::setName);
        if (userDto.getEmail() != null && !userDto.getEmail().equals(updatedUser.getEmail())) {
            if (checkEmailDuplicate(userDto.getEmail())) {
                throw new EmailDuplicateException(String.format("Email %s уже использовался!", userDto.getEmail()));
            }
            updatedUser.setEmail(userDto.getEmail());
        }
        return userMapper.toUserDto(userRepository.save(updatedUser));
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteUser(Long id) {
        User deletedUser = userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь id " + id + " не найден!"));
        userRepository.delete(deletedUser);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public UserDto getUser(Long id) {
        return userMapper.toUserDto(userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь id " + id + " не найден!")));
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserDto).collect(Collectors.toList());
    }

    private boolean checkEmailDuplicate(String email) {
        return userRepository.findAll().stream().anyMatch(u -> u.getEmail().equals(email));
    }

}
