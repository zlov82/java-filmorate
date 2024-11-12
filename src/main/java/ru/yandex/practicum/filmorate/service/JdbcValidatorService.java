package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.GenreStorage;
import ru.yandex.practicum.filmorate.repository.MpaStorage;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JdbcValidatorService implements ValidatorService {
    private final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    @Override
    public boolean validateReleaseDate(LocalDate releaseDate) {
        if (releaseDate.isBefore(MIN_RELEASE_DATE)) {
            log.warn("Релизная дата {} не прошла валидатор", releaseDate);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean validateMpaId(Integer mpaId) {
        List<Mpa> mpaList =  mpaStorage.getMpaList();
        for (Mpa mpa : mpaList) {
            if (mpa.getId().equals(mpaId)) {return true;}
        }
        log.warn("id рейтинга {} не прошел проверку", mpaId);
        return false;
    }

    @Override
    public boolean validateGenreId(Integer genreId) {
        List<Genre> genreList = genreStorage.getGenres();
        for(Genre genre : genreList) {
            if (genre.getId().equals(genreId)) {return true;}
        }
        log.warn("Жанр с id = {} не прошел проверку", genreId);
        return false;
    }
}
