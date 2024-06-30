package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User newUser(User newUser) {
        return userStorage.save(newUser);
    }

    public User update(User updatedUser) {
        return userStorage.update(updatedUser);
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    //как добавление в друзья, удаление из друзей, вывод списка общих друзей


}
