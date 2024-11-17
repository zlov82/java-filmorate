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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        String sql = "INSERT INTO film (name,description,releaseDate,duration,mpa_id) " +
                "VALUES(:f_name,:f_desc, CAST(:f_reldate as date),:f_duration,:f_mpa_id)";
        jdbc.update(sql, sqlParameters, keyHolder, new String[]{"id"});
        newFilm.setId(keyHolder.getKeyAs(Long.class));
        return newFilm;

    }

    @Override
    public Film update(Film updatedFilm) {

        Map<String, Object> params = new HashMap<>();
        params.put("film_id", updatedFilm.getId());
        params.put("n_name", updatedFilm.getName());
        params.put("n_desc", updatedFilm.getDescription());
        params.put("n_reldate", updatedFilm.getReleaseDate().toString());
        params.put("n_duration", updatedFilm.getDuration().toString());
        params.put("n_mpa_id", updatedFilm.getMpa().getId());

        String sql = "UPDATE FILM set name = :n_name, " +
                "description = :n_desc, " +
                "releaseDate = :n_reldate, " +
                "duration = :n_duration, " +
                "mpa_id = :n_mpa_id  " +
                "where id = :film_id";
        jdbc.update(sql, params);
        return updatedFilm;
    }

    @Override
    public Film getFilmById(long filmId) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("film_id", filmId);
            String sql = "select f.id, f.name, f.releasedate, f.duration, f.mpa_id,m.name as mpa_name, f.description, f.rate " +
                    "from film as f, mpa as m " +
                    "where f.mpa_id = m.id "+
                    "and f.id = :film_id";
            return jdbc.queryForObject(sql, namedParams, filmRowMapper);
        } catch (EmptyResultDataAccessException ignored) {
            throw new NotFoundException("Не удалось найти запрошенного фильма");
        }
    }

    @Override
    public Collection<Film> getAll() {
        String sql = "select f.id, f.name, f.releasedate, f.duration, f.mpa_id,m.name as mpa_name, f.description, f.rate " +
                "from film as f, mpa as m " +
                "where f.mpa_id = m.id " +
                "order by f.id";
        return jdbc.query(sql, filmRowMapper);
    }

    @Override
    public void updateRate(long filmId) {
        Map<String, Object> params = new HashMap<>();
        params.put("film_id", filmId);

        String sql = "update FILM " +
                "set rate = (select count(user_id) " +
                "from film_like " +
                "where film_id = :film_id) "+
                "where id = :film_id";
        jdbc.update(sql, params);
    }

    @Override
    public List<Film> getPopularFilms() {
        String sql = "select f.id, f.name, f.releasedate, f.duration, f.mpa_id,m.name as mpa_name, f.description, f.rate " +
                "from film as f, mpa as m " +
                "where f.mpa_id = m.id " +
                "order by f.rate desc";
        return jdbc.query(sql, filmRowMapper);
    }


}
