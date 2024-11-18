package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenresController {

    private final GenreService genreService;

    @GetMapping
    public List<Genre> getAllGenres() {
        log.info("Запрос всех жанров");
        List<Genre> genres = genreService.getAllGenres();
        log.info("Ответ получения всех жанров:\n{}", genres);
        return genres;
    }

    @GetMapping("/{genreId}")
    public Genre getGenreById(@PathVariable int genreId) {
        log.info("Запрос жанра по id = {}", genreId);
        Genre genre = genreService.getGenreById(genreId);
        log.info("Ответ получения жанра:\n{}", genre);
        return genre;
    }


}
