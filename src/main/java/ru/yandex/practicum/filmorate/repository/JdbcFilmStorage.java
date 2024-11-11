package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.mappers.FilmRowMapper;

import java.util.*;

@Repository
@RequiredArgsConstructor
@Qualifier("JdbcFilmStorage")
public class JdbcFilmStorage implements FilmStorage {

    private final NamedParameterJdbcOperations jdbc;
    private final FilmRowMapper filmRowMapper;

    @Override
    public Film save(Film newFilm) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        SqlParameterSource sqlParameters = new MapSqlParameterSource()
                .addValue("f_name", newFilm.getName())
                .addValue("f_desc", newFilm.getDescription())
                .addValue("f_reldate", newFilm.getReleaseDate().toString())
                .addValue("f_duration", newFilm.getDuration().toString())
                .addValue("f_mpa_id", newFilm.getMpa().getId());

        jdbc.update("INSERT INTO film (name,description,releaseDate,duration,mpa_id) VALUES(:f_name,:f_desc, CAST(:f_reldate as date),:f_duration,:f_mpa_id)", sqlParameters, keyHolder, new String[]{"id"});
        newFilm.setId(keyHolder.getKeyAs(Long.class));
        return newFilm;

    }

    @Override
    public Film update(Film updatedFilm) {

        Map<String,Object> params = new HashMap<>();
        params.put("film_id", updatedFilm.getId());
        params.put("n_name", updatedFilm.getName());
        params.put("n_desc", updatedFilm.getDescription());
        params.put("n_reldate", updatedFilm.getReleaseDate().toString());
        params.put("n_duration", updatedFilm.getDuration().toString());
        params.put("n_mpa_id", updatedFilm.getMpa().getId());;

        jdbc.update("UPDATE FILM set name = ':name', description = 'new description', releaseDate = :n_reldate, duration = :n_duration, mpa_id = :n_mpa_id  where id = :film_id",params);
        return updatedFilm;
    }

    @Override
    public Film getFilmById(long filmId) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("film_id", filmId);
            return jdbc.queryForObject("select id, name, releasedate, duration, mpa_id, description from film where id = :film_id", namedParams, filmRowMapper);
        } catch (EmptyResultDataAccessException ignored) {
            throw new NotFoundException("Не удалось найти запрошенного фильма");
        }
    }

    @Override
    public Collection<Film> getAll() {
        return jdbc.query("select id, name, releasedate, duration, mpa_id, description from film order by id",filmRowMapper);
    }
}
