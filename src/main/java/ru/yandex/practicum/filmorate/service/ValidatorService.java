package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.util.Set;

public interface ValidatorService {
    public boolean validateReleaseDate(LocalDate releaseDate);

    public boolean validateMpaId(Integer mpaId);

    public boolean validateGenres(Set<Genre> genres);
}
