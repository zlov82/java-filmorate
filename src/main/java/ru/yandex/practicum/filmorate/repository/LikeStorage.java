package ru.yandex.practicum.filmorate.repository;

public interface LikeStorage {
    void addlike(Long filmId, Long userId);

    void removelike(Long filmId, Long userId);
}
