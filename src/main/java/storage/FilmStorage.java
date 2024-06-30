package storage;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    public Film createNewFilm(Film newFilm);
    public Film updateFilm(Film updatedFilm);
    public Collection<Film> getAllFilms();

}
