package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmsLikes;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Film save(Film newFilm);

    Film update(Film updatedFilm);

    Film getFilmById(long filmId);

    Collection<Film> getAll();

    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);

    List<FilmsLikes> getFilmsLikes();
}
