package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films = new HashMap<>();
    private long filmsCounter = 0L;

    //добавление фильма
    @PostMapping
    public Film createNewFilm(@Valid @RequestBody Film newFilm) {
        log.info("Фильм для добавления {}", newFilm);
        validFilm(newFilm);
        newFilm.setId(getUniqueFilmId());
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    //обновление фильма
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film updatedFilm) {
        log.info("Фильм для обновления {}", updatedFilm);
        if (updatedFilm.getId() == null) {
            throw new ValidationException("Не указан id фильма для обновления");
        }

        Film savedFilm = films.get(updatedFilm.getId());
        if (savedFilm == null) {
            throw new ValidationException("Ошибка поиска фильма для обновления");
        }

        validFilm(updatedFilm);
        films.put(savedFilm.getId(), updatedFilm);

        return updatedFilm;
    }

    //получение всех фильмов
    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Запрос списка фильмов:\n{}",films.values());
        return films.values();
    }

    private long getUniqueFilmId() {
        return ++filmsCounter;
    }

    private void validFilm(Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.debug("Релизная дата {}", film.getReleaseDate());
            throw new ValidationException("Слишком ранняя дата релиза");
        }
    }
}
