package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();
    private long usersCounter = 0L;

    //создание пользователя
    @PostMapping
    public User createUser(@Valid @RequestBody User newUser) {
        log.info("Создание нового пользователя {}", newUser);
        if (newUser.getLogin().contains(" ")) {
            throw new ValidationException("Логин содержит пробелы");
        }

        if (newUser.getName() == null) {
            log.debug("Не указано имя пользователя, будет взято из логина");
            newUser.setName(newUser.getLogin());
        }

        newUser.setId(getUniqueUserId());
        users.put(newUser.getId(), newUser);

        return newUser;
    }

    //обновление пользователя
    @PutMapping
    public User updateUser(@Valid @RequestBody User updatedUser) {
        log.info("Запрос изменения данных пользователя {}", updatedUser);

        if (updatedUser.getId() == null) {
            throw new ValidationException("Не указан id пользователя");
        }

        User savedUser = users.get(updatedUser.getId());
        if (savedUser == null) {
            throw new ValidationException("Пользователь для обновления не найден");
        }

        if (isUserEmailIsBusy(updatedUser.getEmail(), updatedUser.getId())) {
            throw new ValidationException("Email уже занят другим пользователем");
        }

        users.put(updatedUser.getId(), updatedUser);

        return updatedUser;
    }

    //получение списка всех пользователей
    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Запрос списка пользователей:\n{}", users.values());
        return users.values();
    }

    private long getUniqueUserId() {
        return ++usersCounter;
    }

    private boolean isUserEmailIsBusy(String email, long userId) {
        Optional<User> userWithEmail = users.values()
                .stream()
                .filter(user -> user.getEmail().contains(email))
                .filter(user -> user.getId() != userId)
                .findFirst();

        if (userWithEmail.isPresent()) {
            return true;
        }
        return false;
    }


}
