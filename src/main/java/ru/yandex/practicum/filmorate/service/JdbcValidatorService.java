package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.FilmStorage;
import ru.yandex.practicum.filmorate.repository.GenreStorage;
import ru.yandex.practicum.filmorate.repository.MpaStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class JdbcValidatorService implements ValidatorService {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final FilmStorage filmStorage;

    public JdbcValidatorService(MpaStorage mpaStorage, GenreStorage genreStorage, @Qualifier("JdbcFilmStorage") FilmStorage filmStorage) {
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
        this.filmStorage = filmStorage;
    }

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
        List<Mpa> mpaList = mpaStorage.getMpaList();
        for (Mpa mpa : mpaList) {
            if (mpa.getId().equals(mpaId)) {
                return true;
            }
        }
        log.warn("id рейтинга {} не прошел проверку", mpaId);
        return false;
    }

    @Override
    public boolean validateGenres(Set<Genre> genres) {
        List<Genre> allGenres = genreStorage.getGenres();
        List<Integer> rangeOfGenres = new ArrayList<>();
        for (Genre range : allGenres) {
            rangeOfGenres.add(range.getId());
        }

        for (Genre filmGenre : genres) {
            if (!rangeOfGenres.contains(filmGenre.getId())) {
                return false;
            }

        }
        return true;
    }

    @Override
    public void validateFilmById(Long filmId) {
        filmStorage.getFilmById(filmId);
    }

}
