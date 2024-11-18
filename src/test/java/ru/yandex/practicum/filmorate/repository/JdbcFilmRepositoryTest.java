package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mappers.FilmRowMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Import({JdbcFilmStorage.class, FilmRowMapper.class})
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

        Mpa mpa = new Mpa();
        mpa.setId(2);
        mpa.setName("MPA2");
        film.setMpa(mpa);

        return film;
    }

    static Film testFilm2() {
        Film film = new Film();
        film.setId(2L);
        film.setName("Film2");
        film.setDescription("Description2");
        film.setReleaseDate(LocalDate.of(2006, 4, 13));
        film.setDuration(110);

        Mpa mpa = new Mpa();
        mpa.setId(1);
        mpa.setName("MPA1");
        film.setMpa(mpa);

        return film;
    }

    static Film testSaveFilm() {
        Film film = new Film();
        film.setName("Film3");
        film.setDescription("Description3");
        film.setReleaseDate(LocalDate.of(1982, 11, 21));
        film.setDuration(42);

        Mpa mpa = new Mpa();
        mpa.setId(1);
        mpa.setName("MPA1");
        film.setMpa(mpa);

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

        Mpa mpa = new Mpa();
        mpa.setId(2);
        mpa.setName("MPA2");
        updatedFilm.setMpa(mpa);

        filmStorage.update(updatedFilm);
        Film film = filmStorage.getFilmById(updatedFilm.getId());
        assertThat(film)
                .usingRecursiveAssertion()
                .isEqualTo(updatedFilm);
    }

}
