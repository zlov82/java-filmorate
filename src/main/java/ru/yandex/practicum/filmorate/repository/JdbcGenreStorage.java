package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.mappers.GenreRowMapper;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcGenreStorage implements GenreStorage {

    private final NamedParameterJdbcOperations jdbc;
    private final GenreRowMapper genreRowMapper;

    @Override
    public List<Genre> getGenres() {
        Map<String, Object> params = new HashMap<>();
        return jdbc.query("select id, name from genre order by id", params, genreRowMapper);
    }

    @Override
    public Genre getGenreById(int id) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            return jdbc.queryForObject("select id, name from genre where id=:id", params, genreRowMapper);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Жанр с id = {} не найден", id);
            throw new NotFoundException("Жанр не найден");
        }
    }


    @Override
    public boolean saveFilmGenres(long filmId, Set<Genre> genres) {
        deleteFilmGenres(filmId);
        try {
            for (Genre genre : genres) {
                Map<String, Object> params = new HashMap<>();
                params.put("genre_id", genre.getId());
                params.put("film_id", filmId);
                jdbc.update("INSERT INTO  film_genre (film_id,genre_id) VALUES (:film_id,:genre_id)", params);
            }

            return true;
        } catch (DataAccessException ignored) {
            return false;
        }

    }

    @Override
    public void loadFilmGenres(List<Film> films) {
        //Мапа из фильмов для использования в rs
        final Map<Long, Film> filmsById = films.stream().collect(Collectors.toMap(Film::getId, f -> f));

        //Получить лист ID фильмов
        List<String> filmsIds = new ArrayList<>();
        for (Film film : films) {
            filmsIds.add(film.getId().toString());
        }
        String ids = String.join(",", filmsIds);

        String sql = "select fg.film_id, fg.genre_id, g.name " +
                "from film_genre fg, genre g " +
                "where fg.genre_id = g.id " +
                "and film_id in (" + ids + ")";

        jdbc.query(sql, (rs) -> {
            final Film film = filmsById.get(rs.getLong("film_id"));
            Genre genre = new Genre();
            genre.setId(rs.getInt("genre_id"));
            genre.setName(rs.getString("name"));
            film.addGenre(genre);
        });
    }

    @Override
    public void loadFilmGenres(Film film) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("film_id", film.getId());
            String sql = "select id, name " +
                    "from genre " +
                    "where id in (SELECT genre_id FROM FILM_GENRE where film_id = :film_id)";
            List<Genre> genreList = jdbc.query(sql, params, genreRowMapper);
            for (Genre genre : genreList) {
                film.addGenre(genre);
            }
        } catch (EmptyResultDataAccessException ignored) {

        }
    }

    private void deleteFilmGenres(long film_id) {
        SqlParameterSource params = new MapSqlParameterSource("film_id", film_id);
        jdbc.update("DELETE from film_genre where film_id = :film_id", params);
    }

}
