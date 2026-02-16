package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import ru.yandex.practicum.filmorate.dto.film.FilmUpdateInformation;
import ru.yandex.practicum.filmorate.dto.mpa.NewMpaRequest;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.films.Film;
import ru.yandex.practicum.filmorate.model.films.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate.dao")
public class FilmorateDAOTestFilmStorage {
    private final FilmStorage filmStorage;

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
    public void addNewFilm() {
        Film newFilm = new Film();
        newFilm.setReleaseDate(LocalDate.now());
        newFilm.setName("HarryPotter2");
        newFilm.setDuration(100);
        newFilm.setIdMpa(1);
        newFilm.setDescription("efefefeeffeefee");

        Optional<Film> newFilmAdded = filmStorage.addNewFilm(newFilm);
        assertThat(newFilmAdded).isPresent();
        assertThat(filmStorage.getFilmById(newFilmAdded.get().getId())).isPresent().hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("id", newFilmAdded.get().getId()));
        assertEquals(2, filmStorage.getAllFilms().size());

        newFilm.setIdMpa(9999);
        assertThrows(DataIntegrityViolationException.class, () -> filmStorage.addNewFilm(newFilm));
    }

    @Test
    public void getAllFilms() {
        List<Film> films = filmStorage.getAllFilms();
        assertEquals(1, films.size());

        Film newFilm = new Film();
        newFilm.setReleaseDate(LocalDate.now());
        newFilm.setName("HarryPotter2");
        newFilm.setDuration(100);
        newFilm.setIdMpa(1);
        newFilm.setDescription("efefefeeffeefee");
        filmStorage.addNewFilm(newFilm);

        List<Film> films2 = filmStorage.getAllFilms();
        assertEquals(2, films2.size());
    }

    @Test
    public void updateFilm() {
        List<Film> films = filmStorage.getAllFilms();
        Film film = films.getFirst();

        FilmUpdateInformation filmUpdateInformation = new FilmUpdateInformation();
        NewMpaRequest mpa = new NewMpaRequest();
        mpa.setId(2L);
        filmUpdateInformation.setMpa(mpa);
        filmUpdateInformation.setDuration(1);
        filmUpdateInformation.setName("Oracle");

        FilmMapper.updateFilmInformation(film, filmUpdateInformation);
        assertThat(filmStorage.updateFilmInformation(film)).isPresent();

        Optional<Film> updatedFilm = filmStorage.getFilmById(film.getId());
        assertEquals(2, updatedFilm.get().getIdMpa());
        assertEquals(1, updatedFilm.get().getDuration());
        assertEquals("Oracle", updatedFilm.get().getName());

        film.setId(1000L);
        assertThat(filmStorage.updateFilmInformation(film)).isEmpty();
    }

    @Test
    public void getFilmById() {
        List<Film> films = filmStorage.getAllFilms();
        Film film = films.getFirst();

        Optional<Film> filmOptional = filmStorage.getFilmById(film.getId());
        assertThat(filmOptional).isPresent().hasValueSatisfying(filmToCheck -> assertThat(filmToCheck)
                .hasFieldOrPropertyWithValue("id", film.getId()));

        Optional<Film> filmFailCheck = filmStorage.getFilmById(9999L);

        assertThat(filmFailCheck).isEmpty();
    }
}
