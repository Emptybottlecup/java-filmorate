package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.films.Film;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int num) throws SQLException {
        Film film = new Film();

        film.setId(rs.getLong("id"));
        film.setName(rs.getString("name"));
        film.setDuration(rs.getInt("duration"));
        film.setIdMpa(rs.getLong("id_mpa"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());

        return film;
    }
}
