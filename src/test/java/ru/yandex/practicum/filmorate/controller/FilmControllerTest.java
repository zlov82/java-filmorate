package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FilmControllerTest {
    private MockMvc mockMvc;

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
                        .content("""
                                {
                                    "name":"Крепкий орешек",
                                    "description": "Простой мужики разносит в труху кучу террористов",
                                    "releaseDate": "1990-01-01",
                                    "duration": 125
                                }
                                """)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void faultNewFilmReleaseDate() throws Exception {
        Assertions.assertThrows(jakarta.servlet.ServletException.class, () -> {
            this.mockMvc.perform(MockMvcRequestBuilders
                    .post("/films")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                                "name":"Фильм, которого не было",
                                "description": "Главное, что фильм вышел раньше, чем появился кинематограф",
                                "releaseDate": "1701-01-01",
                                "duration": 1
                            }
                            """)
            );
        });
    }

    @Test
    public void faultNewFilmDuration() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name":"Ешь, молись, люби",
                                    "description": "Просто легкий фильм",
                                    "releaseDate": "2000-01-01",
                                    "duration": -1
                                }
                                """)
                )
                .andExpect(status().is4xxClientError());
    }

}




