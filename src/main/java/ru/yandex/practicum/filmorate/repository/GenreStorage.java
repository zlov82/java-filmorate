package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {
    List<Genre> getGenres();

    Genre getGenreById(int id);

    List<Integer> getFilmGenres(long filmId);

    boolean saveFilmGenres(long filmId, Set<Integer> genres);
}
