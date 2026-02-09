package ru.yandex.practicum.filmorate.dao.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.films.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepository extends BaseRepository<Genre> {
    private static final String QUERY_GET_ALL_GENRES = "SELECT * FROM genres ORDER BY id ASC";
    private static final String QUERY_GET_GENRE_BY_ID = "SELECT * FROM genres WHERE id = ?";

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public List<Genre> getAllGenres() {
        return getMany(QUERY_GET_ALL_GENRES);
    }

    public Optional<Genre> getGenreById(long id) {
        return getOne(QUERY_GET_GENRE_BY_ID, id);
    }
}
