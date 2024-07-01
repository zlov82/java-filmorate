package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
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
            throw new NotFoundException("Нет такого фильма");
        }

        User savedUser = userStorage.getUserById(userId);
        if (savedUser == null) {
            throw new NotFoundException("Нет такого пользователя");
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

    public List<Film> getPopularFilms(Long count) {
        Collection<Film> allFilms = filmStorage.getAll();
        if (count > allFilms.size()) {
            log.warn("Всего фильмов {}, а запрошено вывести {}",allFilms.size(),count);
             return allFilms.stream()
                     .sorted(Comparator.comparing(Film::getLikes).reversed())
                     .collect(Collectors.toList());
        } else {
            return allFilms.stream()
                    .sorted(Comparator.comparing(Film::getLikes).reversed())
                    .collect(Collectors.toList())
                    .subList(0,allFilms.size());
        }
    }

    private void validFilm(Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.debug("Релизная дата {}", film.getReleaseDate());
            throw new ValidationException("Слишком ранняя дата релиза");
        }
    }

    private long getUniqueFilmId() {
        return ++filmsCounter;
    }
}
