package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import java.time.LocalDate;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class FilmService {

    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private long filmsCounter = 0L;

    private FilmStorage filmStorage;

    public Film createNewFilm(Film film) {
        validFilm(film);
        film.setId(getUniqueFilmId());
        return filmStorage.save(film);
    }

    public Film updateFilm (Film film) {
        Film savedFilm = filmStorage.getFilmById(film.getId());
        if (savedFilm == null) {
            throw new ValidationException("Ошибка поиска фильма для обновления");
        }

        validFilm(film);
        return filmStorage.update(film);
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
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
