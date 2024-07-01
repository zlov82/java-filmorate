package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long usersCounter = 0L;

    @Override
    public User save(User newUser) {
        newUser.setId(getUniqueUserId());
        newUser.setFriends(new HashSet<>());
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User update(User updatedUser) {
        if (isUserEmailIsBusy(updatedUser.getEmail(), updatedUser.getId())) {
            throw new NotFoundException("Email уже занят другим пользователем");
        }

        User savedUser = users.get(updatedUser.getId());
        if (savedUser == null) {
            throw new ValidationException("Пользователь для обновления не найден");
        }

        users.put(updatedUser.getId(), updatedUser);

        return updatedUser;
    }

    @Override
    public Collection<User> getAll() {
        Collection<User> returnUsers = users.values();
        return returnUsers;
    }

    @Override
    public User getUserById(Long userId) {
        return users.get(userId);
    }

    private long getUniqueUserId() {
        return ++usersCounter;
    }

    private boolean isUserEmailIsBusy(String email, long userId) {
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
