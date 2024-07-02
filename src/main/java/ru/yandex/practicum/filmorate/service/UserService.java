package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Operations;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

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

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    //как добавление в друзья, удаление из друзей, вывод списка общих друзей
    public Collection<User> changeFriends(long userId1, long userId2, Operations action) {
        User savedUser1 = userStorage.getUserById(userId1);
        User savedUser2 = userStorage.getUserById(userId2);

        if (savedUser1 == null || savedUser2 == null) {
            throw new NotFoundException("Один из пользователей не найден");
        }

        Set<Long> user1Friends = savedUser1.getFriends();
        Set<Long> user2Friends = savedUser2.getFriends();

        if (action.equals(Operations.ADD)) {
            user1Friends.add(userId2);
            user2Friends.add(userId1);
        } else if (action.equals(Operations.REMOVE)) {
            user1Friends.remove(userId2);
            user2Friends.remove(userId1);
        }

        savedUser1.setFriends(user1Friends);
        savedUser2.setFriends(user2Friends);

        Collection<User> returnUsers = new ArrayList<>();
        returnUsers.add(userStorage.update(savedUser1));
        returnUsers.add(userStorage.update(savedUser2));

        return returnUsers;
    }

    public Collection<User> getUserFriends(Long userId) {
        User savedUser = userStorage.getUserById(userId);
        if (savedUser == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        Set<Long> userFriendsId = savedUser.getFriends();
        Collection<User> friendsList = new ArrayList<>();

        for (Long friendId : userFriendsId) {
            friendsList.add(userStorage.getUserById(friendId));
        }

        return friendsList;
    }

    public Collection<User> getMutualFriends(Long id, Long otherId) {
        User savedUser = userStorage.getUserById(id);
        if (savedUser == null) {
            return null;
        }

        Set<Long> userFriends = savedUser.getFriends();
        if (userFriends.isEmpty()) {
            return null;
        }

        User savedUser2 = userStorage.getUserById(otherId);
        if (savedUser2 == null) {
            return null;
        }

        Set<Long> user2Friends = savedUser2.getFriends();
        if (user2Friends.isEmpty()) {
            return null;
        }

        Set<Long> mutualListId = new HashSet<>(userFriends);
        mutualListId.retainAll(user2Friends);
        mutualListId.remove(otherId);

        Collection<User> mutualList = new ArrayList<>();
        for (Long userId : mutualListId) {
            mutualList.add(userStorage.getUserById(userId));
        }

        return mutualList;

    }
}
