package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmsLikes;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Operations;
import ru.yandex.practicum.filmorate.repository.FilmStorage;
import ru.yandex.practicum.filmorate.repository.GenreStorage;
import ru.yandex.practicum.filmorate.repository.JdbcMpaStorage;

import java.util.*;

@Slf4j
@Service
@Qualifier("JdbcFilmService")
public class JdbcFilmService implements FilmService {

    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final ValidatorService validatorService;
    private final JdbcMpaStorage mpaStorage;

    public JdbcFilmService(@Qualifier("JdbcFilmStorage") FilmStorage filmStorage, GenreStorage genreStorage, ValidatorService validatorService, JdbcMpaStorage jdbcMpaStorage) {
        this.filmStorage = filmStorage;
        this.genreStorage = genreStorage;
        this.validatorService = validatorService;
        this.mpaStorage = jdbcMpaStorage;
    }

    @Override
    public Film createNewFilm(Film film) {
        if (!validatorService.validateReleaseDate(film.getReleaseDate())) {
            throw new ValidationException("Слишком ранняя дата релиза");
        }

        if (film.getMpa() == null) {
            throw new ValidationException("Не указан рейтинг");
        }
        if (!validatorService.validateMpaId(film.getMpa().getId())) {
            throw new ValidationException("Рейтинг не прошёл проверку");
        }

        if (film.getGenres() != null) {
            if (!validatorService.validateGenres(film.getGenres())) {
                throw new ValidationException("Жарны не прошли валидацию");
            }
        }

        Film savedFilm = filmStorage.save(film);
        film.setId(savedFilm.getId());

        if (film.getGenres() != null) {
            genreStorage.saveFilmGenres(film.getId(), film.getGenres());
        }

        return this.getFilmById(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        Film savedFilm = this.getFilmById(film.getId());
        if (film.getReleaseDate() != null) {
            if (!validatorService.validateReleaseDate(film.getReleaseDate())) {
                throw new ValidationException("Слишком ранняя дата релиза");
            }
        } else {
            film.setReleaseDate(savedFilm.getReleaseDate());
        }

        if (film.getMpa() != null) {
            if (!validatorService.validateMpaId(film.getMpa().getId())) {
                throw new ValidationException("Рейтинг не прошёл проверку");
            }
        } else {
            film.setMpa(savedFilm.getMpa());
        }

        if (film.getDescription() == null) {
            film.setDescription(savedFilm.getDescription());
        }

        if (film.getGenres() != null) {
            if (!validatorService.validateGenres(film.getGenres())) {
                throw new ValidationException("Жанры не прошли валидацию");
            }
        }

        filmStorage.update(film);

        if (film.getGenres() != null) {
            genreStorage.saveFilmGenres(film.getId(), film.getGenres());
        }
        return this.getFilmById(film.getId());
    }

    @Override
    public Collection<Film> getAll() {
        //берем все фильмы
        Collection<Film> filmsList = filmStorage.getAll();
        //в цикле плюсуем к каждому жанры
        for (Film film : filmsList) {
            film.setMpa(mpaStorage.getMpaById(film.getMpa().getId()));
            film.setGenres((LinkedHashSet<Genre>)genreStorage.getFilmGenres(film.getId()));
        }
        return filmsList;
    }

    @Override
    public Film getFilmById(Long id) {
        Film film = filmStorage.getFilmById(id);
        film.setMpa(mpaStorage.getMpaById(film.getMpa().getId()));
        film.setGenres((LinkedHashSet<Genre>) genreStorage.getFilmGenres(id));
        return film;
    }

    @Override
    public Film changeFilmsLikes(Long filmId, Long userId, Operations action) {
        Film film = this.getFilmById(filmId);

        if (action.equals(Operations.ADD)) {
            filmStorage.addLike(filmId, userId);
        } else {
            filmStorage.removeLike(filmId, userId);
        }
        return film;
    }

    @Override
    public List<Film> getPopularFilms(Long count) {
        List<FilmsLikes> filmsLikes = filmStorage.getFilmsLikes();
        List<Film> filmList = new ArrayList<>();

        if (filmsLikes.size() > count) {
            filmsLikes = filmsLikes.subList(0, Math.toIntExact(count));
        }

        for (FilmsLikes filmsLike : filmsLikes) {
            filmList.add(this.getFilmById(filmsLike.getFilmId()));
        }

        return filmList;
    }
}
