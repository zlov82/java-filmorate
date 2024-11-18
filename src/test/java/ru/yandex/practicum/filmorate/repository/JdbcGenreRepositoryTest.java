package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.mappers.GenreRowMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({JdbcGenreStorage.class, GenreRowMapper.class})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@DisplayName("Тестирование жанров")
public class JdbcGenreRepositoryTest {
    private final JdbcGenreStorage jdbcGenreStorage;

    static Genre testGenre1() {
        Genre genre = new Genre();
        genre.setId(1);
        genre.setName("Комедия");
        return genre;
    }

    static Genre testGenre2() {
        Genre genre = new Genre();
        genre.setId(2);
        genre.setName("Драма");
        return genre;
    }

    static List<Genre> testGenres = new ArrayList<>(Arrays.asList(testGenre1(), testGenre2()));

    @Test
    @DisplayName("Получение жанра по Id")
    public void getGenreById() {
        Genre genre = jdbcGenreStorage.getGenreById(1);

        assertThat(genre)
                .usingRecursiveAssertion()
                .isEqualTo(testGenre1());
    }


    @Test
    @DisplayName("Получение всех жанров")
    public void getAllGenres() {
        List<Genre> genreList = jdbcGenreStorage.getGenres();

        assertThat(genreList)
                .hasSize(2)
                .isEqualTo(testGenres);

    }
}
