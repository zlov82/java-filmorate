package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Film save(Film newFilm);

    Film update(Film updatedFilm);

    Film getFilmById(long filmId);

    Collection<Film> getAll();

    void updateRate(long filmId);

    List<Film> getPopularFilms();
}
