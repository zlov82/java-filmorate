package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Operations;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    //получение списка всех пользователей
    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Запрос списка пользователей:");
        Collection<User> returnUsers = userService.getAll();
        log.info(returnUsers.toString());
        return returnUsers;
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/{userId}/friends")
    public Collection<User> getUserFriends(@PathVariable Long userId) {
        return userService.getUserFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getMutualFriends(@PathVariable Long id,
                                             @PathVariable Long otherId) {
        return userService.getMutualFriends(id, otherId);
    }

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

        return userService.newUser(newUser);
    }

    //обновление пользователя
    @PutMapping
    public User updateUser(@Valid @RequestBody User updatedUser) {
        log.info("Запрос изменения данных пользователя {}", updatedUser);
        if (updatedUser.getId() == null) {
            throw new ValidationException("Не указан id пользователя");
        }
        return userService.update(updatedUser);
    }

    //добавление в друзья
    @PutMapping("/{userId}/friends/{friendId}")
    public Collection<User> addFriend(@PathVariable Long userId,
                                      @PathVariable Long friendId) {
        return userService.changeFriends(userId, friendId, Operations.ADD);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public Collection<User> deleteFriend(@PathVariable Long userId,
                                         @PathVariable Long friendId) {
        return userService.changeFriends(userId, friendId, Operations.REMOVE);
    }


}
