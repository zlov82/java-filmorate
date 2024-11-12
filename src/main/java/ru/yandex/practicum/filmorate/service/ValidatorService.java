package ru.yandex.practicum.filmorate.service;

import java.time.LocalDate;

public interface ValidatorService {
    public boolean validateReleaseDate(LocalDate releaseDate);

    public boolean validateMpaId(Integer mpaId);

    public boolean validateGenreId(Integer genreId);
}
