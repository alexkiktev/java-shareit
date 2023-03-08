package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User user, Long id);

    void deleteUser(Long id);

    List<User> getAllUsers();

    User getUser(Long id);

    boolean checkEmailDuplicate(String email);

}
