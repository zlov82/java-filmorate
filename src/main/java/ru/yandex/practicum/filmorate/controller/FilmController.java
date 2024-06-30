package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import storage.FilmStorage;
import storage.InMemoryFilmStorage;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmStorage filmStorage = new InMemoryFilmStorage();

    //добавление фильма
    @PostMapping
    public Film createNewFilm(@Valid @RequestBody Film newFilm) {
        log.info("Фильм для добавления {}", newFilm);
        return filmStorage.createNewFilm(newFilm);
    }

    //обновление фильма
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film updatedFilm) {
        log.info("Фильм для обновления {}", updatedFilm);
        if (updatedFilm.getId() == null) {
            throw new ValidationException("Не указан id фильма для обновления");
        }

        return filmStorage.updateFilm(updatedFilm);
    }

    //получение всех фильмов
    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Запрос списка фильмов:");
        Collection<Film> returnFilms = filmStorage.getAllFilms();
        log.info(returnFilms.toString());
        return returnFilms;
    }

}
