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

    private final Map<Integer, Film> films = new HashMap<>();
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    //добавление фильма
    @PostMapping
    public Film createNewFilm(@Valid @RequestBody Film newFilm) {
        log.info("Фильм для добавления {}", newFilm);
        if (isValidFilm(newFilm)) {
            newFilm.setId(getUniqueFilmId());
            films.put(newFilm.getId(), newFilm);
            return newFilm;
        }
        return null;
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

        if (isValidFilm(updatedFilm)) {
            savedFilm.setName(updatedFilm.getName());
            if (updatedFilm.getDescription() != null) {
                savedFilm.setDescription(updatedFilm.getDescription());
            }
            if (updatedFilm.getReleaseDate() != null) {
                savedFilm.setReleaseDate(updatedFilm.getReleaseDate());
            }
            if (updatedFilm.getDuration() != null) {
                savedFilm.setDuration(updatedFilm.getDuration());
            }
            return savedFilm;
        }
        return null;
    }

    //получение всех фильмов
    @GetMapping
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    private int getUniqueFilmId() {
        int maxFilmId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++maxFilmId;
    }

    private boolean isValidFilm(Film film) {
        if (film.getDescription().length() > 200) {
            log.debug("Длина описания фильма {}", film.getDescription().length());
            throw new ValidationException("Слишком длинное описание фильма");
        }
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.debug("Релизная дата {}", film.getReleaseDate());
            throw new ValidationException("Слишком ранняя дата релиза");
        }
        if (!film.getDuration().isPositive()) {
            log.debug("Продолжительность {}", film.getDuration());
            throw new jakarta.validation.ValidationException("Продолжительность фильма меньше нуля");
        }
        return true;
    }
}
