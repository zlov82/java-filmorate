package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    public User save(User newUser);
    public User update(User updatedUser);
    public Collection<User> getAll();
}
