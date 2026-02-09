package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.films.GenreAndFilm;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GenreAndFilmRowMapper implements RowMapper<GenreAndFilm> {
    @Override
    public GenreAndFilm mapRow(ResultSet rs, int num) throws SQLException {
        GenreAndFilm genreAndFilm = new GenreAndFilm();

        genreAndFilm.setIdFilm(rs.getLong("id_film"));
        genreAndFilm.setIdGenre(rs.getLong("id_genre"));

        return genreAndFilm;
    }
}
