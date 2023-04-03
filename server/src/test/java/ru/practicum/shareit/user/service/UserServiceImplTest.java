package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.EmailDuplicateException;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class UserServiceImplTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserServiceImpl userServiceImpl;

    @Test
    @Sql("classpath:cleanup.sql")
    void successful_createUserTest() {
        UserDto userDto = new UserDto(null, "a@a.ru", "Alex");
        UserDto controlUser = new UserDto(1L, "a@a.ru", "Alex");

        UserDto savedUserDto = userServiceImpl.createUser(userDto);

        Assertions.assertEquals(controlUser.getId(), savedUserDto.getId());
        Assertions.assertEquals(controlUser.getEmail(), savedUserDto.getEmail());
        Assertions.assertEquals(controlUser.getName(), savedUserDto.getName());
    }

    @Test
    @Sql("classpath:cleanup.sql")
    void throwException_whenNameEmpty_createUserTest() {
        UserDto userDto = new UserDto(null, null, "Alex");

        Assertions.assertThrows(RuntimeException.class, () -> userServiceImpl.createUser(userDto));
    }

    @Test
    @Sql("classpath:cleanup.sql")
    void throwException_whenEmailEmpty_createUserTest() {
        UserDto userDto = new UserDto(null, "a@a.ru", null);

        Assertions.assertThrows(RuntimeException.class, () -> userServiceImpl.createUser(userDto));
    }

    @Test
    @Sql("classpath:cleanup.sql")
    void throwException_whenEmailDuplicate_createUserTest() {
        UserDto userDto = new UserDto(null, "test@test.ru", "Alex");
        UserDto userEmailDuplicateDto = new UserDto(null, "test@test.ru", "Roman");

        userServiceImpl.createUser(userDto);

        Assertions.assertThrows(RuntimeException.class,
                () -> userServiceImpl.createUser(userEmailDuplicateDto));
    }

    @Test
    @Sql("classpath:cleanup.sql")
    void throwException_whenInvalidEmail_createUserTest() {
        UserDto userDto = new UserDto(null, "email.ru", "Alex");

        Assertions.assertThrows(RuntimeException.class, () -> userServiceImpl.createUser(userDto));
    }

    @Test
    @Sql("classpath:cleanup.sql")
    void successful_updateUserTest() {
        UserDto userInputDto = new UserDto(null, "test@test.ru", "Alex");
        UserDto userForUpdateDto = new UserDto(null, "new@test.ru", "Alex");
        UserDto controlUserDto = new UserDto(1L, "new@test.ru", "Alex");

        userServiceImpl.createUser(userInputDto);
        UserDto updatedUserDto = userServiceImpl.updateUser(userForUpdateDto, 1L);

        Assertions.assertEquals(controlUserDto.getId(), updatedUserDto.getId());
        Assertions.assertEquals(controlUserDto.getEmail(), updatedUserDto.getEmail());
        Assertions.assertEquals(controlUserDto.getName(), updatedUserDto.getName());
    }

    @Test
    @Sql("classpath:cleanup.sql")
    void throwException_whenEmailDuplicate_updateUserTest() {
        UserDto userInputDto = new UserDto(null, "new@test.ru", "Alex");
        UserDto userInputDto2 = new UserDto(null, "test@test.ru", "Alex");

        userServiceImpl.createUser(userInputDto);
        UserDto secondUser = userServiceImpl.createUser(userInputDto2);

        UserDto userDuplicateEmailDto = new UserDto(secondUser.getId(), "new@test.ru", "Alex");

        Assertions.assertThrows(EmailDuplicateException.class,
                () -> userServiceImpl.updateUser(userDuplicateEmailDto, userDuplicateEmailDto.getId()));
    }

    @Test
    @Sql("classpath:cleanup.sql")
    void throwException_whenUserNotFound_updateUserTest() {
        UserDto userInputDto = new UserDto(null, "new@test.ru", "Alex");
        Long controlId = 2L;
        UserDto userInputDto2 = new UserDto(controlId, "test@test.ru", "Alex");

        userServiceImpl.createUser(userInputDto);

        Assertions.assertThrows(RuntimeException.class, () -> userServiceImpl.updateUser(userInputDto2, controlId));
    }

    @Test
    @Sql("classpath:cleanup.sql")
    void successful_deleteUser() {
        UserDto userDto = new UserDto(null, "a@a.ru", "Alex");

        UserDto savedUser = userServiceImpl.createUser(userDto);
        userServiceImpl.deleteUser(savedUser.getId());

        Assertions.assertTrue(userServiceImpl.getAllUsers().isEmpty());
    }

    @Test
    @Sql("classpath:cleanup.sql")
    void throwException_whenUserNotFound_deleteUser() {
        UserDto userDto = new UserDto(null, "a@a.ru", "Alex");
        Long controlId = 2L;

        UserDto savedUser = userServiceImpl.createUser(userDto);

        Assertions.assertThrows(RuntimeException.class, () -> userServiceImpl.deleteUser(controlId));
    }

}