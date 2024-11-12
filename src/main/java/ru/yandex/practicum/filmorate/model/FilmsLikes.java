package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class FilmsLikes {
    private long filmId;
    private long likesCount;
}
