package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.LinkedHashSet;
import java.util.List;

public interface GenreStorage {
    List<Genre> getGenres();

    Genre getGenreById(int id);

    LinkedHashSet<Genre> getFilmGenres(long filmId);

    boolean saveFilmGenres(long filmId, LinkedHashSet<Genre> genres);
}
