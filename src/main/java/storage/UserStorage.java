package storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    public User createUser(User newUser);
    public User updateUser(User updatedUser);
    public Collection<User> getAllUsers();
}
