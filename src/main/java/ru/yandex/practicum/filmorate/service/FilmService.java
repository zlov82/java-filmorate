package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Operations;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private long filmsCounter = 0L;

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film createNewFilm(Film film) {
        validFilm(film);
        film.setId(getUniqueFilmId());
        film.setLikes(0L);
        return filmStorage.save(film);
    }

    public Film updateFilm(Film film) {
        Film savedFilm = filmStorage.getFilmById(film.getId());
        if (savedFilm == null) {
            throw new NotFoundException("Ошибка поиска фильма для обновления");
        }
        validFilm(film);
        return filmStorage.update(film);
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    public Film changeFilmsLikes(Long filmId, Long userId, Operations action) {
        Film savesFilm = filmStorage.getFilmById(filmId);
        if (savesFilm == null) {
            return null;
        }

        User savedUser = userStorage.getUserById(userId);
        if (savedUser == null) {
            return null;
        }

        Long likes = savesFilm.getLikes();
        if (action.equals(Operations.ADD)) {
            likes++;
        } else {
            likes--;
        }
        savesFilm.setLikes(likes);
        filmStorage.update(savesFilm);

        return savesFilm;
    }

    public Collection<Film> getPopularFilms(Integer count) {
        Collection<Film> allFilms = filmStorage.getAll();
        if (allFilms.size() < count) {
            count = allFilms.size();
        }
        return allFilms.stream().sorted(Comparator.comparing(Film::getLikes).reversed()).collect(Collectors.toList()).subList(0, count);
    }

    private void validFilm(Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            //log.debug("Релизная дата {}", film.getReleaseDate());
            throw new ValidationException("Слишком ранняя дата релиза");
        }
    }

    private long getUniqueFilmId() {
        return ++filmsCounter;
    }
}
