package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JdbcMpaService implements MpaService {
    private final MpaStorage mpaStorage;

    @Override
    public Mpa getMpaById(int id) {
        return mpaStorage.getMpaById(id);
    }

    @Override
    public List<Mpa> getAllMpa() {
        return mpaStorage.getMpaList();
    }
}
