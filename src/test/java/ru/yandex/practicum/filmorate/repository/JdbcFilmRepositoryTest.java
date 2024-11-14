package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmsLikes;
import ru.yandex.practicum.filmorate.model.filmEntry.MpaEntity;
import ru.yandex.practicum.filmorate.repository.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.repository.mappers.FilmsLikesRowMapper;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Import({JdbcFilmStorage.class, FilmRowMapper.class, FilmsLikes.class, FilmsLikesRowMapper.class})
@DisplayName("Проверка фильмов")
public class JdbcFilmRepositoryTest {
    private final JdbcFilmStorage filmStorage;

    static Film testFilm() {
        Film film = new Film();
        film.setId(1L);
        film.setName("Film1");
        film.setDescription("Description1");
        film.setReleaseDate(LocalDate.of(1988, 7, 12));
        film.setDuration(133);

        MpaEntity mpaEntity = new MpaEntity();
        mpaEntity.setId(2);
        film.setMpa(mpaEntity);

        return film;
    }

    static Film testFilm2() {
        Film film = new Film();
        film.setId(2L);
        film.setName("Film2");
        film.setDescription("Description2");
        film.setReleaseDate(LocalDate.of(2006, 4, 13));
        film.setDuration(110);

        MpaEntity mpaEntity = new MpaEntity();
        mpaEntity.setId(1);
        film.setMpa(mpaEntity);

        return film;
    }

    static Film testSaveFilm() {
        Film film = new Film();
        film.setName("Film3");
        film.setDescription("Description3");
        film.setReleaseDate(LocalDate.of(1982, 11, 21));
        film.setDuration(42);

        MpaEntity mpaEntity = new MpaEntity();
        mpaEntity.setId(1);
        film.setMpa(mpaEntity);

        return film;
    }

    static Collection<Film> testFilms = new ArrayList<>(Arrays.asList(testFilm(), testFilm2()));

    @Test
    @DisplayName("Получение фильма по id")
    public void testFindById() {
        Film film = filmStorage.getFilmById(2);
        assertThat(film)
                .usingRecursiveAssertion()
                .isEqualTo(testFilm2());
    }

    @Test
    @DisplayName("Получение всех фильмов")
    public void testFindAll() {
        Collection<Film> filmList = filmStorage.getAll();
        assertThat(filmList)
                .hasSize(2)
                .usingRecursiveComparison()
                .isEqualTo(testFilms);
    }

    @Test
    @DisplayName("Добавление нового фильма")
    public void testSave() {
        Film film = filmStorage.save(testSaveFilm());
        Film savedFilm = testSaveFilm();
        savedFilm.setId(3L);
        assertThat(film)
                .usingRecursiveAssertion()
                .isEqualTo(savedFilm);

        Collection<Film> listFilms = filmStorage.getAll();
        assertThat(listFilms)
                .hasSize(3);
    }

    @Test
    @DisplayName("Обновление фильма")
    public void testUpdate() {
        Film updatedFilm = testFilm2();
        updatedFilm.setName("UpdatedName");
        updatedFilm.setDescription("UpdatedDescription");
        MpaEntity mpaEntity = new MpaEntity();
        mpaEntity.setId(updatedFilm.getMpa().getId() + 1);
        updatedFilm.setMpa(mpaEntity);

        filmStorage.update(updatedFilm);
        Film film = filmStorage.getFilmById(updatedFilm.getId());
        assertThat(film)
                .usingRecursiveAssertion()
                .isEqualTo(updatedFilm);
    }

    @Test
    @DisplayName("Добавление лайка")
    public void addLike() {
        FilmsLikes fl = new FilmsLikes();
        fl.setFilmId(1);
        fl.setLikesCount(2);
        List<FilmsLikes> testFilmLikes = new ArrayList<>(Arrays.asList(fl));

        filmStorage.addLike(1, 1);
        filmStorage.addLike(1, 2);
        List<FilmsLikes> savedLikes = filmStorage.getFilmsLikes();

        assertThat(savedLikes)
                .usingRecursiveAssertion()
                .isEqualTo(testFilmLikes);
    }

    @Test
    @DisplayName("Удаление лайка")
    public void removeLike() {
        FilmsLikes fl = new FilmsLikes();
        fl.setFilmId(2);
        fl.setLikesCount(1);
        List<FilmsLikes> testFilmLikes = new ArrayList<>(Arrays.asList(fl));

        filmStorage.addLike(2, 1);
        filmStorage.addLike(2, 2);
        filmStorage.removeLike(2, 1);
        List<FilmsLikes> savedLikes = filmStorage.getFilmsLikes();

        assertThat(savedLikes)
                .usingRecursiveAssertion()
                .isEqualTo(testFilmLikes);
    }


}
