package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.mappers.UserRowMapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
@Qualifier("JdbcRepository")
@RequiredArgsConstructor
public class JdbcUserStorage implements UserStorage {

    private final NamedParameterJdbcOperations jdbc;
    private final UserRowMapper userRowMapper;

    @Override
    public User save(User newUser) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", newUser.getName())
                .addValue("email", newUser.getEmail())
                .addValue("login", newUser.getLogin())
                .addValue("birthday",newUser.getBirthday());
        jdbc.update("INSERT INTO users (email,login,name,birthday) VALUES (:email,:login,:name,:birthday)",params,keyHolder,new String[]{"id"});
        newUser.setId(keyHolder.getKeyAs(Long.class));
        return newUser;
    }

    @Override
    public User update(User updatedUser) {
        Map<String,Object> params = new HashMap<>();
        params.put("name", updatedUser.getName());
        params.put("email", updatedUser.getEmail());
        params.put("login", updatedUser.getLogin());
        params.put("birthday",updatedUser.getBirthday());
        params.put("user_id", updatedUser.getId());
        jdbc.update("UPDATE users SET email =:email, login =:login, name =:name,birthday=:birthday where id=:user_id", params);
        return this.getUserById(updatedUser.getId());
    }

    @Override
    public Collection<User> getAll() {
        return jdbc.query("SELECT id, login, email, birthday, name FROM users",userRowMapper);
    }

    @Override
    public User getUserById(Long userId) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", userId);
            return jdbc.queryForObject("SELECT id, login, email, birthday, name FROM users WHERE id = :id",params,userRowMapper);
        } catch (EmptyResultDataAccessException ignored) {
            throw new NotFoundException("Пользователь не найден");
        }
    }
}
