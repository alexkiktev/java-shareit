package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.MarkerValidation;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto createUser(@RequestBody @Validated({MarkerValidation.OnCreate.class}) UserDto userDto) {
        log.info("Получен запрос на создание пользователя");
        return userService.createUser(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@RequestBody @Validated({MarkerValidation.OnUpdate.class}) UserDto userDto,
                              @PathVariable Long id) {
        log.info("Получен запрос на обновление данных пользователя");
        return userService.updateUser(userDto, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Получен запрос на удаление пользователя id {}", id);
        userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        log.info("Получен запрос данных пользователя id {}", id);
        return userService.getUser(id);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Получен запрос всех пользователей");
        return userService.getAllUsers();
    }

}
