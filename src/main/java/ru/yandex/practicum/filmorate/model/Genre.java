package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Genre {
    @Size(min = 1)
    public Integer id;
    @NotBlank
    public String name;
}
