package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Operations;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    //получение всех фильмов
    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Запрос списка фильмов:");
        Collection<Film> returnFilms = filmService.getAll();
        log.info(returnFilms.toString());
        return returnFilms;
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10") Long count) {
        return filmService.getPopularFilms(count);
    }

    //добавление фильма
    @PostMapping
    public Film createNewFilm(@Valid @RequestBody Film newFilm) {
        log.info("Фильм для добавления {}", newFilm);
        return filmService.createNewFilm(newFilm);
    }

    //обновление фильма
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film updatedFilm) {
        log.info("Фильм для обновления {}", updatedFilm);
        if (updatedFilm.getId() == null) {
            throw new ValidationException("Не указан id фильма для обновления");
        }
        return filmService.updateFilm(updatedFilm);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film addLike(@PathVariable Long filmId,
                        @PathVariable Long userId) {
        return filmService.changeFilmsLikes(filmId, userId, Operations.ADD);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Film removeLike(@PathVariable Long filmId,
                           @PathVariable Long userId) {
        return filmService.changeFilmsLikes(filmId, userId, Operations.REMOVE);
    }


}
