package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Operations;
import java.util.Collection;
import java.util.List;


public interface FilmService {

    public Film createNewFilm(Film film);

    public Film updateFilm(Film film);

    public Collection<Film> getAll();

    public Film getFilmById(Long id);

    public Film changeFilmsLikes(Long filmId, Long userId, Operations action);

    public List<Film> getPopularFilms(Long count);

}
