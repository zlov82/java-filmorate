package ru.yandex.practicum.filmorate.repository.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.filmEntry.MpaEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(LocalDate.parse(rs.getString("releasedate")));
        film.setDuration(rs.getInt("duration"));

        MpaEntity mpaEntity = new MpaEntity();
        mpaEntity.setId(rs.getInt("mpa_id"));
        film.setMpa(mpaEntity);
/*
        IdEntity mpaEntity = new IdEntity();
        mpaEntity.setId(rs.getInt("mpa_id"));
        film.setMpa(mpaEntity);
*/

        return film;
    }
}
