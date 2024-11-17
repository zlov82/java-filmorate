package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Operations;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FriendsStorage;
import ru.yandex.practicum.filmorate.repository.UserStorage;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Qualifier("JdbcUserService")
public class JdbcUserService implements UserService {

    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    public JdbcUserService(@Qualifier("JdbcRepository") UserStorage userStorage, FriendsStorage friendsStorage) {
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
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
        User user = this.getUserById(userId1);
        User friend = this.getUserById(userId2);

        if (action.equals(Operations.ADD)) {
            friendsStorage.addFriend(user, friend);
        } else {
            friendsStorage.removeFriend(user, friend);
        }
        return this.getUserFriends(userId1);
    }

    @Override
    public Collection<User> getUserFriends(Long userId) {
        User user = userStorage.getUserById(userId);
        return userStorage.getUserFriends(user);
    }

    @Override
    public Collection<User> getMutualFriends(Long id, Long otherId) {
        Collection<User> user1Friends = this.getUserFriends(id);
        Collection<User> user2Friends = this.getUserFriends(otherId);

        Collection<User> commonFriends = new ArrayList<>();

        for (User user1Friend : user1Friends) {
            for (User user2Friend : user2Friends) {
                if (user1Friend.getId().equals(user2Friend.getId())) {
                    commonFriends.add(user1Friend);
                }
            }
        }
        return commonFriends;
    }
}
