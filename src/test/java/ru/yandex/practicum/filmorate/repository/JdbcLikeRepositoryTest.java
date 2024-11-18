package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.mappers.FilmRowMapper;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@JdbcTest
@DisplayName("Лайки и дизлайки фильмов")
@Import({JdbcLikeStorage.class, JdbcFilmStorage.class, FilmRowMapper.class})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JdbcLikeRepositoryTest {
    private final JdbcLikeStorage likeStorage;
    private final JdbcFilmStorage filmStorage;

    @Test
    @DisplayName("Добавление лайка")
    public void addLike() {
        likeStorage.addlike(1L, 1L);
        likeStorage.addlike(1L, 2L);
        filmStorage.updateRate(1L);
        Film film = filmStorage.getFilmById(1L);

        assertThat(film)
                .hasFieldOrPropertyWithValue("likesCounter", 2L);
    }

    @Test
    @DisplayName("Удаление лайка")
    public void removeLike() {
        likeStorage.addlike(1L, 1L);
        likeStorage.addlike(1L, 2L);
        filmStorage.updateRate(1L);

        likeStorage.removelike(1L, 1L);
        filmStorage.updateRate(1L);
        Film film = filmStorage.getFilmById(1L);

        assertThat(film)
                .hasFieldOrPropertyWithValue("likesCounter", 1L);

    }
}
