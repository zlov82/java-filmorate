package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mappers.MpaRowMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcMpaStorage implements MpaStorage {

    private final NamedParameterJdbcOperations jdbc;
    private final MpaRowMapper mpaRowMapper;

    @Override
    public Mpa getMpaById(Integer id) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            return jdbc.queryForObject("select id, name from mpa where id = :id", params, mpaRowMapper);
        } catch (EmptyResultDataAccessException ignored) {
            log.warn("Ошибка запроса рейтинга с id = {}", id);
            throw new NotFoundException("Нет данных о запрошенном рейтинге");
        }
    }

    @Override
    public List<Mpa> getMpaList() {
        Map<String, Object> params = new HashMap<>();
        return jdbc.query("select id, name from mpa order by id", params, mpaRowMapper);
    }
}
