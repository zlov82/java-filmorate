package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    Mpa getMpaById(Integer id);

    List<Mpa> getMpaList();
}
