package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreStorage;
import ru.yandex.practicum.filmorate.repository.JdbcGenreStorage;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JdbcGenreService implements GenreService {

    private final GenreStorage genreStorage;

    @Override
    public Genre findGenreById(int id) {
        return genreStorage.getGenreById(id);
    }

    @Override
    public List<Genre> findGenres() {
        return genreStorage.getGenres();
    }
}
