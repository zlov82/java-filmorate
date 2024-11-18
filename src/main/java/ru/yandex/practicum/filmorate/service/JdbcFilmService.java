package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Operations;
import ru.yandex.practicum.filmorate.repository.FilmStorage;
import ru.yandex.practicum.filmorate.repository.GenreStorage;
import ru.yandex.practicum.filmorate.repository.LikeStorage;

import java.util.*;

@Slf4j
@Service
@Qualifier("JdbcFilmService")
public class JdbcFilmService implements FilmService {

    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final ValidatorService validatorService;
    private final LikeStorage likeStorage;

    public JdbcFilmService(@Qualifier("JdbcFilmStorage") FilmStorage filmStorage, GenreStorage genreStorage, ValidatorService validatorService, LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.genreStorage = genreStorage;
        this.validatorService = validatorService;
        this.likeStorage = likeStorage;
    }

    @Override
    public Film createNewFilm(Film film) {
        if (!validatorService.validateReleaseDate(film.getReleaseDate())) {
            throw new ValidationException("Слишком ранняя дата релиза");
        }

        if (film.getMpa() == null) {
            throw new ValidationException("Не указан рейтинг");
        }
        if (!validatorService.validateMpaId(film.getMpa().getId())) {
            throw new ValidationException("Рейтинг не прошёл проверку");
        }

        if (film.getGenres() != null) {
            if (!validatorService.validateGenres(film.getGenres())) {
                throw new ValidationException("Жарны не прошли валидацию");
            }
        }

        Film savedFilm = filmStorage.save(film);
        film.setId(savedFilm.getId());

        if (film.getGenres() != null) {
            genreStorage.saveFilmGenres(film.getId(), film.getGenres());
        }

        return this.getFilmById(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        Film savedFilm = this.getFilmById(film.getId());
        if (film.getReleaseDate() != null) {
            if (!validatorService.validateReleaseDate(film.getReleaseDate())) {
                throw new ValidationException("Слишком ранняя дата релиза");
            }
        } else {
            film.setReleaseDate(savedFilm.getReleaseDate());
        }

        if (film.getMpa() != null) {
            if (!validatorService.validateMpaId(film.getMpa().getId())) {
                throw new ValidationException("Рейтинг не прошёл проверку");
            }
        } else {
            film.setMpa(savedFilm.getMpa());
        }

        if (film.getDescription() == null) {
            film.setDescription(savedFilm.getDescription());
        }

        if (film.getGenres() != null) {
            if (!validatorService.validateGenres(film.getGenres())) {
                throw new ValidationException("Жанры не прошли валидацию");
            }
        }

        filmStorage.update(film);

        if (film.getGenres() != null) {
            genreStorage.saveFilmGenres(film.getId(), film.getGenres());
        }
        return this.getFilmById(film.getId());
    }

    @Override
    public Collection<Film> getAll() {
        Collection<Film> filmsList = filmStorage.getAll();
        genreStorage.loadFilmGenres((List<Film>) filmsList);
        return filmsList;
    }

    @Override
    public Film getFilmById(Long id) {
        Film film = filmStorage.getFilmById(id);
        genreStorage.loadFilmGenres(film);
        return film;
    }

    @Override
    public Film changeFilmsLikes(Long filmId, Long userId, Operations action) {
        validatorService.validateFilmById(filmId);

        if (action.equals(Operations.ADD)) {
            likeStorage.addlike(filmId, userId);

        } else {
            likeStorage.removelike(filmId, userId);
        }
        filmStorage.updateRate(filmId);
        return this.getFilmById(filmId);
    }

    @Override
    public List<Film> getPopularFilms(Long count) {
        List<Film> films = filmStorage.getPopularFilms();
        if (films.size() > count) {
            films = films.subList(0, Math.toIntExact(count));
        }
        return films;
    }
}
