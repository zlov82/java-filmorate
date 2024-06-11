package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.controller.adapter.LocalDateAdapter;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FilmControllerTest {
    private MockMvc mockMvc;

    GsonBuilder gsonBuilder = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter());

    private Gson gson = gsonBuilder.create();

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new FilmController()).build();
    }

    @Test
    public void successGetRequest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/films"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void successNewFilm() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(createFilm(
                                "Крепкий орешек",
                                "Простой мужики разносит в труху кучу террористов",
                                LocalDate.of(1990, 1, 1),
                                125
                        )))
                )
                .andExpect(status().isOk());
    }

    @Test
    public void faultNewFilmReleaseDate() {
        Assertions.assertThrows(jakarta.servlet.ServletException.class, () -> {
                    this.mockMvc.perform(MockMvcRequestBuilders
                            .post("/films")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(gson.toJson(createFilm(
                                    "Фильм, которого не было",
                                    "Главное, что фильм вышел раньше, чем появился кинематограф",
                                    LocalDate.of(1701, 01, 01),
                                    1)))
                    );
                }
        );
    }


    @Test
    public void faultNewFilmDuration() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(createFilm(
                                "Ешь, молись, люби",
                                "Просто легкий фильм",
                                LocalDate.of(2000, 01, 01),
                                -1)))
                )
                .andExpect(status().is4xxClientError());
    }


    private Film createFilm(String name, String description, LocalDate date, Integer duration) {
        Film film = new Film();

        film.setName(name);
        film.setDescription(description);
        film.setReleaseDate(date);
        film.setDuration(duration);

        return film;
    }

}




