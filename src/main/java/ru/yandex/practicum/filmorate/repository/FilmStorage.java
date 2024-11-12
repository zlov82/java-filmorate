package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmsLikes;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    public Film save(Film newFilm);

    public Film update(Film updatedFilm);

    public Film getFilmById(long filmId);

    public Collection<Film> getAll();

    public void addLike(long filmId, long userId);

    public void removeLike(long filmId, long userId);

    public List<FilmsLikes> getFilmsLikes();
}
