package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User save(User newUser);

    User update(User updatedUser);

    Collection<User> getAll();

    User getUserById(Long userId);
}
