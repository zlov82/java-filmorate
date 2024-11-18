package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Genre {
    @Size(min = 1)
    public Integer id;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String name;
}
