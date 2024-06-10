package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Instant;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    //создание пользователя
    @PostMapping
    public User createUser(@RequestBody User newUser) {
        log.info("Создание нового пользователя {}", newUser);
        if (newUser.getEmail().isEmpty()) {
            throw new ValidationException("Электронная почта не должна быть пустой");
        }
        if (!newUser.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не содержит @");
        }
        if (newUser.getLogin().isEmpty() || newUser.getLogin().contains(" ")) {
            throw new ValidationException("Некорректный логин пользователя");
        }
        if (newUser.getBirthday().after(Date.from(Instant.now()))) {
            throw new ValidationException("Дата рождения не может быть в будущем");
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
    public User updateUser(@RequestBody User updatedUser) {
        log.info("Запрос изменения данных пользователя {}", updatedUser);

        if (updatedUser.getId() == null) {
            throw new ValidationException("Не указан id пользователя");
        }

        if (updatedUser.getEmail() != null) {
            if (!updatedUser.getEmail().contains("@")) {
                throw new ValidationException("Email должен быть указан и содержать @");
            }
            if (isUserEmailIsBusy(updatedUser.getEmail(), updatedUser.getId())) {
                throw new ValidationException("Email уже занят другим пользователем");
            }
        }

        if (updatedUser.getBirthday() != null) {
            if (updatedUser.getBirthday().after(Date.from(Instant.now()))) {
                throw new ValidationException("День рождения не может быть в будущем");
            }
        }

        User savedUser = users.get(updatedUser.getId());
        if (savedUser == null) {
            throw new ValidationException("Пользователь для обновления не найден");
        }
        if (updatedUser.getEmail() != null) {
            savedUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getBirthday() != null) {
            savedUser.setBirthday(updatedUser.getBirthday());
        }
        if (updatedUser.getLogin() != null) {
            savedUser.setLogin(updatedUser.getLogin());
        }
        if (updatedUser.getName() != null) {
            savedUser.setName(updatedUser.getName());
        }
        return savedUser;
    }

    //получение списка всех пользователей
    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Запрос списка пользователей. Пользователей {}", users.size());
        return users.values();
    }

    private int getUniqueUserId() {
        int maxUserId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++maxUserId;
    }

    private boolean isUserEmailIsBusy(String email, int userId) {
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
