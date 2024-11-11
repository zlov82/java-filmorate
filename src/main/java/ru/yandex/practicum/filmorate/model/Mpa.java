package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class Mpa {
    @Positive
    private Integer id;
    @NotBlank
    private String name;
}
