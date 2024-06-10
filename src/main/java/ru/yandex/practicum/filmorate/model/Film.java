package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;

/**
 * Film.
 */
@Data
public class Film {
    Integer id;
    String name;
    String description;
    Instant releaseDate;
    Duration duration;

}
