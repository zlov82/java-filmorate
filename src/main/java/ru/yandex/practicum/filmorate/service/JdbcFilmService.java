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

import java.util.*;

@Slf4j
@Service
@Qualifier("JdbcFilmService")
public class JdbcFilmService implements FilmService {

    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final ValidatorService validatorService;

    public JdbcFilmService(@Qualifier("JdbcFilmStorage") FilmStorage filmStorage, GenreStorage genreStorage, ValidatorService validatorService) {
        this.filmStorage = filmStorage;
        this.genreStorage = genreStorage;
        this.validatorService = validatorService;
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
        Film savedFilm = filmStorage.save(film);
        film.setId(savedFilm.getId());

        if (film.getGenres() != null) {
            Set<Integer> genreList = convertGenreToInteger(film.getGenres());
            genreStorage.saveFilmGenres(film.getId(), genreList);
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

        filmStorage.update(film);

        if (film.getGenres() != null) {
            Set<Integer> genreList = convertGenreToInteger(film.getGenres());
            genreStorage.saveFilmGenres(film.getId(), genreList);
        }
        return this.getFilmById(film.getId());
    }

    @Override
    public Collection<Film> getAll() {
        //берем все фильмы
        Collection<Film> filmsList = filmStorage.getAll();
        //в цикле плюсуем к каждому жанры
        for (Film film : filmsList) {
            film.setGenres(loadGenresByFilmId(film.getId()));
        }
        return filmsList;
    }

    @Override
    public Film getFilmById(Long id) {
        Film film = filmStorage.getFilmById(id);
        //добавляем жарны
        film.setGenres(loadGenresByFilmId(id));
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

    private Set<Integer> convertGenreToInteger(Set<Genre> genresSet) {
        try {
            if (genresSet.isEmpty()) {
                throw new ValidationException("Нет жанров");
            }
            Set<Integer> genrelist = new HashSet<>();
            for (Genre genre : genresSet) {
                if (!validatorService.validateGenreId(genre.getId())) {
                    throw new ValidationException("Жанра не существует");
                }
                genrelist.add(genre.getId());
            }
            return genrelist;
        } catch (NullPointerException ignored) {
            return null;
        }
    }

    private LinkedHashSet<Genre> loadGenresByFilmId(Long filmId) {
        List<Integer> filmGenres = genreStorage.getFilmGenres(filmId);
        LinkedHashSet<Genre> genresSet = new LinkedHashSet<>();
        for (Integer genreId : filmGenres) {
            Genre genre = new Genre();
            genre.setId(genreId);
            genresSet.add(genre);
        }
        return genresSet;
    }
}
