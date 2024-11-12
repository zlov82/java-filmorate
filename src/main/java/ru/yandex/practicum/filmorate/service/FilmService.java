package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Operations;

import java.util.Collection;
import java.util.List;


public interface FilmService {

    Film createNewFilm(Film film);

    Film updateFilm(Film film);

    Collection<Film> getAll();

    Film getFilmById(Long id);

    Film changeFilmsLikes(Long filmId, Long userId, Operations action);

    List<Film> getPopularFilms(Long count);

}
