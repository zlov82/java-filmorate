package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    User save(User newUser);

    User update(User updatedUser);

    Collection<User> getAll();

    User getUserById(Long userId);

    List<User> getUserFriends(User user);
}
