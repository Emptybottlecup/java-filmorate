package ru.yandex.practicum.filmorate.dao.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.films.GenreAndFilm;

import java.util.List;
import java.util.Optional;

@Repository
public class GenresAndFilmRepository extends BaseRepository<GenreAndFilm> {
    private static final String QUERY_GET_ALL_GENRES_OF_FILM = "SELECT * FROM genres_and_films WHERE id_film = ?";
    private static final String QUERY_INSERT_GENRE_AND_FILM = "INSERT INTO genres_and_films (id_film, id_genre) VALUES (?,?) ";
    private static final String QUERY_DELETE_ALL_GENRE_AND_FILM_BY_ID = "DELETE FROM genres_and_films WHERE id_film = ?";

    public GenresAndFilmRepository(JdbcTemplate jdbc, RowMapper<GenreAndFilm> mapper) {
        super(jdbc, mapper);
    }

    public List<GenreAndFilm> getAllGenreOfFilm(long id) {
        return getMany(QUERY_GET_ALL_GENRES_OF_FILM, id);
    }

    public Optional<GenreAndFilm> insertGenreAndFilm(long filmId, long genreId) {
        if (update(QUERY_INSERT_GENRE_AND_FILM, filmId, genreId)) {
            GenreAndFilm genreAndFilm = new GenreAndFilm();
            genreAndFilm.setIdFilm(filmId);
            genreAndFilm.setIdGenre(genreId);
            return Optional.of(genreAndFilm);
        }
        return Optional.empty();
    }

    public boolean deleteAllGenresOfFilmById(long filmId) {
        return update(QUERY_DELETE_ALL_GENRE_AND_FILM_BY_ID, filmId);
    }
}
