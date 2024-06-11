package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.Date;

@Data
public class User {
    Integer id;
    @NotNull
    @NotBlank
    @Email
    String email;
    @NotNull
    @NotBlank
    String login;
    String name;
    Date birthday;
}
