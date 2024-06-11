package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    Integer id;
    @NotNull
    @NotBlank
    String name;
    String description;
    LocalDate releaseDate;
    Duration duration;
}
