package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {
    List<Genre> getGenres();

    Genre getGenreById(int id);

    boolean saveFilmGenres(long filmId, Set<Genre> genres);

    void loadFilmGenres(List<Film> films);

    void loadFilmGenres(Film film);
}
