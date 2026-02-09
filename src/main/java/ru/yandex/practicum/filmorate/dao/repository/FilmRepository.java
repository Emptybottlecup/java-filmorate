package ru.yandex.practicum.filmorate.dao.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.films.Film;
import ru.yandex.practicum.filmorate.model.users.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Optional;

@Repository
public class FilmRepository extends BaseRepository<Film> implements FilmStorage {
    private final String QUERY_GET_ALL_FILMS = "SELECT * FROM films";
    private final String QUERY_GET_FILM_BY_ID = "SELECT * FROM films WHERE id = ?";
    private final String QUERY_INSERT_NEW_FILM = "INSERT INTO films (name, description, id_mpa, release_date, duration)" +
            " VALUES(?, ?, ?, ?, ?)";
    private final String QUERY_UPDATE_FILM_INFORMATION = "UPDATE films SET name = ?, description = ?, id_mpa = ?," +
            "release_date = ?, duration = ? WHERE id = ?";

    public FilmRepository(JdbcTemplate jdbc, FilmRowMapper mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<Film> getAllFilms() {
        return getMany(QUERY_GET_ALL_FILMS);
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        return getOne(QUERY_GET_FILM_BY_ID, id);
    }

    @Override
    public Optional<Film> addNewFilm(Film film) {
        Optional<Long> id = insert(QUERY_INSERT_NEW_FILM,
                film.getName(),
                film.getDescription(),
                film.getIdMpa(),
                film.getReleaseDate(),
                film.getDuration());

        if (id.isEmpty()) {
            return Optional.empty();
        }

        film.setId(id.get());

        return Optional.of(film);
    }

    @Override
    public Optional<Film> updateFilmInformation(Film film) {
        boolean isUpdated = update(QUERY_UPDATE_FILM_INFORMATION,
                film.getName(),
                film.getDescription(),
                film.getIdMpa(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId()
        );

        if(!isUpdated) {
            return Optional.empty();
        }
        return Optional.of(film);
    }
}
