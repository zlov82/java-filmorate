package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.filmEntry.GenreEntity;
import ru.yandex.practicum.filmorate.model.Operations;
import ru.yandex.practicum.filmorate.repository.FilmStorage;
import ru.yandex.practicum.filmorate.repository.GenreStorage;
import ru.yandex.practicum.filmorate.repository.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@Qualifier("JdbcFilmService")
public class JdbcFilmService implements FilmService {

    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final ValidatorService validatorService;
    private final UserStorage userStorage;

    public JdbcFilmService(@Qualifier("JdbcFilmStorage") FilmStorage filmStorage, GenreStorage genreStorage, ValidatorService validatorService, @Qualifier("JdbcRepository") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.genreStorage = genreStorage;
        this.validatorService = validatorService;
        this.userStorage = userStorage;
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

        Set<Integer> genreList = convertGenreToInteger(film.getGenres());
        Film savedFilm = filmStorage.save(film);
        // получить id фильма и сохранить жанры
        film.setId(savedFilm.getId());
        genreStorage.saveFilmGenres(film.getId(), genreList);

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        Film savedFilm = filmStorage.getFilmById(film.getId()); //если запрощенного фильма не будет - код ответа 404
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

        filmStorage.update(film);

        if (film.getGenres() != null) {
            Set<Integer> genreList = convertGenreToInteger(film.getGenres());
            genreStorage.saveFilmGenres(film.getId(), genreList);
        } else {
            film.setGenres(loadGenresByFilmId(savedFilm.getId()));
        }

        return film;
    }

    @Override
    public Collection<Film> getAll() {
        //берем все фильмы
        Collection<Film> filmsList = filmStorage.getAll();
        //в цикле плюсуем к каждому жанры
        for(Film film : filmsList) {
            film.setGenres(loadGenresByFilmId(film.getId()));
        }
        return filmsList;
    }

    @Override
    public Film getFilmById(Long id) {
        Film film = filmStorage.getFilmById(id);
        film.setGenres(loadGenresByFilmId(id));
        return film;
    }

    @Override
    public Film changeFilmsLikes(Long filmId, Long userId, Operations action) {
        Film film = filmStorage.getFilmById(filmId);
        film.setGenres(loadGenresByFilmId(film.getId()));

        if (action.equals(Operations.ADD)) {
            filmStorage.addLike(filmId, userId);
        }else {
            filmStorage.removeLike(filmId, userId);
        }
        return film;
    }

    @Override
    public List<Film> getPopularFilms(Long count) {
        return List.of();
    }

    private Set<Integer> convertGenreToInteger(Set<GenreEntity> genresSet) {
        if (genresSet.isEmpty()) {
            throw new ValidationException("Нет жанров");
        }
        Set<Integer> genrelist = new HashSet<>();
        for(GenreEntity genre : genresSet) {
            if (!validatorService.validateGenreId(genre.getId())) {
                throw new ValidationException("Жанра не существует");
            }
            genrelist.add(genre.getId());
        }
        return genrelist;
    }

    private HashSet<GenreEntity> loadGenresByFilmId (Long filmId) {
        List<Integer> filmGenres = genreStorage.getFilmGenres(filmId);
        HashSet<GenreEntity> genresSet = new HashSet<>();
        for (Integer genre : filmGenres) {
            GenreEntity entity = new GenreEntity();
            entity.setId(genre);
            genresSet.add(entity);
        }
        return genresSet;
    }
}
