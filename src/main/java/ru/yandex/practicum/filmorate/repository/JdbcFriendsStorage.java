package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JdbcFriendsStorage implements FriendsStorage {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public void addFriend(User user, User friend) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user.getId());
        params.put("friend_id", friend.getId());
        String sql = "MERGE INTO FRIENDSHIP (user_id, friend_id) " +
                "KEY (user_id, friend_id) " +
                "VALUES (:user_id,:friend_id)";
        jdbc.update(sql, params);
    }

    @Override
    public void removeFriend(User user, User friend) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user.getId());
        params.put("friend_id", friend.getId());
        String sql = "DELETE FROM FRIENDSHIP " +
                "WHERE user_id=:user_id " +
                "AND friend_id=:friend_id";
        jdbc.update(sql, params);
    }
}
