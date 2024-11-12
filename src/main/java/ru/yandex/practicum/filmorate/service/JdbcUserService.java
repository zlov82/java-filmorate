package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Operations;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmStorage;
import ru.yandex.practicum.filmorate.repository.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@Qualifier("JdbcUserService")
public class JdbcUserService implements UserService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    public JdbcUserService(@Qualifier("JdbcRepository")UserStorage userStorage, @Qualifier("JdbcFilmStorage")FilmStorage filmStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    @Override
    public User newUser(User newUser) {
        return userStorage.save(newUser);
    }

    @Override
    public User update(User updatedUser) {
        User saveduser = userStorage.getUserById(updatedUser.getId());
        if (updatedUser.getName() == null) {
            updatedUser.setName(saveduser.getName());
        }
        if (updatedUser.getBirthday() == null) {
            updatedUser.setBirthday(saveduser.getBirthday());
        }
        return userStorage.update(updatedUser);
    }

    @Override
    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    @Override
    public Collection<User> changeFriends(long userId1, long userId2, Operations action) {
        return List.of();
    }

    @Override
    public Collection<User> getUserFriends(Long userId) {
        return List.of();
    }

    @Override
    public Collection<User> getMutualFriends(Long id, Long otherId) {
        return null;
    }
}
