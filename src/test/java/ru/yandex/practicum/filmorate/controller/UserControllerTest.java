package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.controller.adapter.LocalDateAdapter;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {
    private MockMvc mockMvc;

    GsonBuilder gsonBuilder = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter());

    private Gson gson = gsonBuilder.create();

    @BeforeEach
    public void setUp() {
        //this.mockMvc = MockMvcBuilders.standaloneSetup(new UserController()).build();
    }

    @Test
    public void successGetRequest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/users"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void successCreateNewUser() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(createUser(
                                null,
                                "andy.kozlov@gmail.com",
                                "zlov",
                                "Andrey Kozlov",
                                LocalDate.of(1982, 11, 21)
                        ))))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void successUpdateUser() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(createUser(
                                null,
                                "andy.kozlov@gmail.com",
                                "zlv",
                                "AK",
                                LocalDate.of(1982, 11, 21)
                        ))))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("AK"));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(createUser(
                                1L,
                                "andy.kozlov@gmail.com",
                                "zlov",
                                "Andrey Kozlov",
                                LocalDate.of(1982, 11, 21)
                        ))))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.login").value("zlov"))
                .andExpect(jsonPath("$.name").value("Andrey Kozlov"));
    }

    @Test
    public void createUserNotName() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(createUser(
                                null,
                                "andy.kozlov@gmail.com",
                                "zlov",
                                null,
                                LocalDate.of(1982, 11, 21)
                        ))))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name").value("zlov"));

    }

    private User createUser(Long id, String email, String login, String name, LocalDate birthday) {
        User user = new User();

        user.setId(id);
        user.setEmail(email);
        user.setLogin(login);
        user.setName(name);
        user.setBirthday(birthday);

        return user;

    }


}