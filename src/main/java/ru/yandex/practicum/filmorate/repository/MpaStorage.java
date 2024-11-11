package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    public Mpa getMpaById(Integer id);
    public List<Mpa> getMpaList();
}
