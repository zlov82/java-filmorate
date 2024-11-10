package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Operations;
import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;

public interface UserService {

    public User newUser(User newUser);

    public User update(User updatedUser);

    public Collection<User> getAll();

    public User getUserById(Long id);

    public Collection<User> changeFriends(long userId1, long userId2, Operations action);

    public Collection<User> getUserFriends(Long userId);

    public Collection<User> getMutualFriends(Long id, Long otherId);
}
