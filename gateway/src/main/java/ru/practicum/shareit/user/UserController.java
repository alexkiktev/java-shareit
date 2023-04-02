package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.utils.MarkerValidation;

@RestController
@Slf4j
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody
                                                 @Validated({MarkerValidation.OnCreate.class}) UserDto userDto) {
        log.info("Получен запрос на создание пользователя");
        return userClient.createUser(userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody @Validated({MarkerValidation.OnUpdate.class}) UserDto userDto,
                              @PathVariable Long id) {
        log.info("Получен запрос на обновление данных пользователя");
        return userClient.updateUser(userDto, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Получен запрос на удаление пользователя id {}", id);
        userClient.deleteUser(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable Long id) {
        log.info("Получен запрос данных пользователя id {}", id);
        return userClient.getUser(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Получен запрос всех пользователей");
        return userClient.getAllUsers();
    }

}
