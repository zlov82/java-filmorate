package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcLikeStorage implements LikeStorage {
    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public void addlike(Long filmId, Long userId) {
        SqlParameterSource sqlParameters = new MapSqlParameterSource("film_id", filmId).addValue("user_id", userId);
        jdbc.update("MERGE INTO film_like (film_id, user_id) KEY (film_id, user_id) VALUES (:film_id,:user_id)",
                sqlParameters);
    }

    @Override
    public void removelike(Long filmId, Long userId) {
        SqlParameterSource sqlParameters = new MapSqlParameterSource("film_id", filmId).addValue("user_id", userId);
        jdbc.update("DELETE FROM film_like WHERE film_id = :film_id AND user_id =:user_id", sqlParameters);
    }
}
