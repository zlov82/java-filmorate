package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Operations;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserService {

    User newUser(User newUser);

    User update(User updatedUser);

    Collection<User> getAll();

    User getUserById(Long id);

    Collection<User> changeFriends(long userId1, long userId2, Operations action);

    Collection<User> getUserFriends(Long userId);

    Collection<User> getMutualFriends(Long id, Long otherId);
}
