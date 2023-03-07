package ru.practicum.shareit.user.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.exception.EmailDuplicateException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
@Repository
public class InMemoryUserStorage implements UserStorage{

    private final HashMap<Long, User> users;
    private Long id = 0L;

    @Override
    public User createUser(User user) {
        if (checkEmailDuplicate(user.getEmail())) {
            throw new EmailDuplicateException(String.format("Email %s уже использовался!", user.getEmail()));
        }
        user.setId(++id);
        users.put(user.getId(), user);
        log.info("Создан пользователь: " + user.getName());
        return user;
    }

    @Override
    public User updateUser(User user, Long id) {
        if (users.containsKey(id)) {
            User updatedUser = users.get(id);
            if (user.getName() != null) {
                updatedUser.setName(user.getName());
            }
            if (user.getEmail() != null && !user.getEmail().equals(updatedUser.getEmail())) {
                if (checkEmailDuplicate(user.getEmail())) {
                    throw new EmailDuplicateException(String.format("Email %s уже использовался!", user.getEmail()));
                }
                updatedUser.setEmail(user.getEmail());
            }
            log.info("Обновлен пользователь: " + updatedUser.getName());
            return updatedUser;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь id " + id + " не найден!");
        }
    }

    @Override
    public void deleteUser(Long id) {
        if (users.containsKey(id)) {
            users.remove(id);
            log.info("Пользователь id {} удален", id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь id " + id + " не найден!");
        }
    }

    @Override
    public User getUser(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь id " + id + " не найден!");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean checkEmailDuplicate(String email) {
        for (Map.Entry<Long, User> mapEntries: users.entrySet()) {
            if (mapEntries.getValue().getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

}
