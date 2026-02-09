package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import ru.yandex.practicum.filmorate.dao.repository.GenreRepository;
import ru.yandex.practicum.filmorate.dao.repository.GenresAndFilmRepository;
import ru.yandex.practicum.filmorate.model.films.Film;
import ru.yandex.practicum.filmorate.model.films.Genre;
import ru.yandex.practicum.filmorate.model.films.GenreAndFilm;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate.dao")
public class FilmorateDAOTestGenresAndFilmRepository {
    private final FilmStorage filmStorage;
    private final GenresAndFilmRepository genresAndFilmRepository;
    private final GenreRepository genreRepository;

    @BeforeEach
    public void addFilms() {
        Film film = new Film();
        film.setReleaseDate(LocalDate.now());
        film.setName("HarryPotter");
        film.setDuration(100);
        film.setIdMpa(1);
        film.setDescription("efefefeeffeefee");
        filmStorage.addNewFilm(film);
    }

    @Test
    public void insertGenreAndFilm() {
        List<Film> films = filmStorage.getAllFilms();
        List<Genre> genres = genreRepository.getAllGenres();

        Optional<GenreAndFilm> genreAndFilm = genresAndFilmRepository.insertGenreAndFilm(films.get(0).getId(), genres.get(0).getId());
        assertThat(genreAndFilm).isPresent();

        List<GenreAndFilm> genresOfFilm = genresAndFilmRepository.getAllGenreOfFilm(films.get(0).getId());
        assertEquals(1, genresOfFilm.size());
        assertEquals(genresOfFilm.getFirst().getIdGenre(), genres.get(0).getId());
        assertEquals(genresOfFilm.getFirst().getIdFilm(), films.get(0).getId());

        Optional<GenreAndFilm> secondeGenreAndFilm = genresAndFilmRepository.insertGenreAndFilm(films.get(0).getId(), genres.get(1).getId());
        assertThat(secondeGenreAndFilm).isPresent();

        List<GenreAndFilm> genresOfFilm2 = genresAndFilmRepository.getAllGenreOfFilm(films.get(0).getId());
        assertEquals(2, genresOfFilm2.size());

        assertThrows(DuplicateKeyException.class, () -> genresAndFilmRepository.insertGenreAndFilm(films.get(0).getId(), genres.get(1).getId()));

        assertThrows(DataIntegrityViolationException.class, () -> genresAndFilmRepository.insertGenreAndFilm(9999L, genres.get(1).getId()));
    }

    @Test
    public void getAllGenreOfFilm() {
        List<Film> films = filmStorage.getAllFilms();
        List<Genre> genres = genreRepository.getAllGenres();

        Optional<GenreAndFilm> genreAndFilm = genresAndFilmRepository.insertGenreAndFilm(films.get(0).getId(), genres.get(0).getId());

        List<GenreAndFilm> genresOfFilm = genresAndFilmRepository.getAllGenreOfFilm(films.get(0).getId());
        assertEquals(1, genresOfFilm.size());

        Optional<GenreAndFilm> secondeGenreAndFilm = genresAndFilmRepository.insertGenreAndFilm(films.get(0).getId(), genres.get(1).getId());
        assertThat(secondeGenreAndFilm).isPresent();
        List<GenreAndFilm> genresOfFilm2 = genresAndFilmRepository.getAllGenreOfFilm(films.get(0).getId());
        assertEquals(2, genresOfFilm2.size());
    }

    @Test
    public void deleteAllGenresOfFilmById() {
        List<Film> films = filmStorage.getAllFilms();
        List<Genre> genres = genreRepository.getAllGenres();

        genresAndFilmRepository.insertGenreAndFilm(films.get(0).getId(), genres.get(0).getId());
        genresAndFilmRepository.insertGenreAndFilm(films.get(0).getId(), genres.get(1).getId());

        assertThat(genresAndFilmRepository.deleteAllGenresOfFilmById(films.get(0).getId())).isTrue();

        List<GenreAndFilm> genreAndFilms = genresAndFilmRepository.getAllGenreOfFilm(films.get(0).getId());

        assertEquals(0, genreAndFilms.size());
    }
}
