package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreStorage;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JdbcGenreService implements GenreService {

    private final GenreStorage genreStorage;

    @Override
    public Genre getGenreById(int id) {
        return genreStorage.getGenreById(id);
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreStorage.getGenres();
    }
}
