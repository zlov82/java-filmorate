package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film save(Film newFilm);

    Film update(Film updatedFilm);

    Film getFilmById(long filmId);

    Collection<Film> getAll();
}
