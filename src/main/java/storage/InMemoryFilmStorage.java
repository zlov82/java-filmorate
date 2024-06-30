package storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage{
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films = new HashMap<>();
    private long filmsCounter = 0L;


    @Override
    public Film createNewFilm(Film newFilm) {
        validFilm(newFilm);
        newFilm.setId(getUniqueFilmId());
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public Film updateFilm(Film updatedFilm) {
        Film savedFilm = films.get(updatedFilm.getId());
        if (savedFilm == null) {
            throw new ValidationException("Ошибка поиска фильма для обновления");
        }

        validFilm(updatedFilm);
        films.put(savedFilm.getId(), updatedFilm);

        return updatedFilm;
    }

    @Override
    public Collection<Film> getAllFilms() {
        Collection<Film> returnFilms = films.values();
        return returnFilms;
    }

    private long getUniqueFilmId() {
        return ++filmsCounter;
    }

    private void validFilm(Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            //log.debug("Релизная дата {}", film.getReleaseDate());
            throw new ValidationException("Слишком ранняя дата релиза");
        }
    }
}
