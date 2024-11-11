package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {
    public List<Genre> getGenres();
    public Genre getGenreById(int id);
    public List<Integer> getFilmGenres(long filmId);
    public boolean saveFilmGenres(long film_id, Set<Integer> genres);
}
