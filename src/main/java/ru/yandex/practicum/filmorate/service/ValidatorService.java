package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.util.Set;

public interface ValidatorService {
    boolean validateReleaseDate(LocalDate releaseDate);

    boolean validateMpaId(Integer mpaId);

    boolean validateGenres(Set<Genre> genres);
}
