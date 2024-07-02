package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films = new HashMap<>();
    private long filmsCounter = 0L;

    @Override
    public Film save(Film newFilm) {
        validFilm(newFilm);
        newFilm.setId(getUniqueFilmId());
        newFilm.setLikesCounter(0L);
        newFilm.setUsersWhoLike(new HashSet<>());
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public Film update(Film updatedFilm) {
        validFilm(updatedFilm);
        films.put(updatedFilm.getId(), updatedFilm);
        return updatedFilm;
    }

    @Override
    public Film getFilmById(long filmId) {
        return films.get(filmId);
    }

    @Override
    public Collection<Film> getAll() {
        Collection<Film> returnFilms = films.values();
        return returnFilms;
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
