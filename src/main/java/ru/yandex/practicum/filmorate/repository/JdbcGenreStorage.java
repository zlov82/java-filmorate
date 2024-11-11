package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.mappers.GenreRowMapper;

import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcGenreStorage implements GenreStorage {

    private final NamedParameterJdbcOperations jdbc;
    private final GenreRowMapper genreRowMapper;

    @Override
    public List<Genre> getGenres() {
        Map<String, Object> params = new HashMap<>();
         return jdbc.query("select id, name from genre order by id",params, genreRowMapper);
    }

    @Override
    public Genre getGenreById(int id) {
        try{
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            return jdbc.queryForObject("select id, name from genre where id=:id",params, genreRowMapper);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Жанр с id = {} не найден",id);
            throw new NotFoundException("Жанр не найден");
        }
    }

    @Override
    public List<Integer> getFilmGenres(long filmId) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("film_id", filmId);
            return jdbc.queryForList("select genre_id from film_genre where film_id = :film_id", params,Integer.class);
        } catch (EmptyResultDataAccessException ignored) {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean saveFilmGenres(long film_id, Set<Integer> genres) {
        deleteFilmGenres(film_id);
        //потом добавим заново
        for (Integer genreId : genres) {
            Map<String, Object> params = new HashMap<>();
            params.put("genre_id", genreId);
            params.put("film_id", film_id);
            Integer intRes = jdbc.update("INSERT INTO  film_genre (film_id,genre_id) VALUES (:film_id,:genre_id)",params);
        }
        return false;
    }

    private void deleteFilmGenres(long film_id) {
        SqlParameterSource params = new MapSqlParameterSource("film_id", film_id);
        jdbc.update("DELETE from film_genre where film_id = :film_id", params);
    }

}
