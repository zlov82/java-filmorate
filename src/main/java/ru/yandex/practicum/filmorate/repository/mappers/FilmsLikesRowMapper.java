package ru.yandex.practicum.filmorate.repository.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmsLikes;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmsLikesRowMapper implements RowMapper<FilmsLikes> {

    @Override
    public FilmsLikes mapRow(ResultSet rs, int rowNum) throws SQLException {
        FilmsLikes filmsLikes = new FilmsLikes();
        filmsLikes.setFilmId(rs.getLong("film_id"));
        filmsLikes.setLikesCount(rs.getLong("likes_count"));
        return filmsLikes;
    }
}
