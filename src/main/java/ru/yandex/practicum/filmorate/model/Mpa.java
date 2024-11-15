package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class Mpa {
    @Positive
    private Integer id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
}
