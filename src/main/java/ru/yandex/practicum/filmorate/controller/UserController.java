package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import storage.FilmStorage;
import storage.InMemoryUserStorage;
import storage.UserStorage;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    UserStorage userStorage = new InMemoryUserStorage();

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

        return userStorage.createUser(newUser);
    }

    //обновление пользователя
    @PutMapping
    public User updateUser(@Valid @RequestBody User updatedUser) {
        log.info("Запрос изменения данных пользователя {}", updatedUser);

        if (updatedUser.getId() == null) {
            throw new ValidationException("Не указан id пользователя");
        }

        return userStorage.updateUser(updatedUser);
    }

    //получение списка всех пользователей
    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Запрос списка пользователей:");
        Collection<User> returnUsers = userStorage.getAllUsers();
        log.info(returnUsers.toString());
        return returnUsers;
    }


}
