package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.dao.repository.GenreRepository;
import ru.yandex.practicum.filmorate.model.films.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate.dao")
public class FilmorateDAOTestGenreRepository {
    private final GenreRepository genreRepository;

    @Test
    public void getAllGenres() {
        List<Genre> genres = genreRepository.getAllGenres();

        assertEquals(6, genres.size());
    }

    @Test
    public void getGenreById() {
        List<Genre> genres = genreRepository.getAllGenres();

        Optional<Genre> genre = genreRepository.getGenreById(genres.get(0).getId());

        assertThat(genre).isPresent();
        assertEquals(genres.get(0).getName(), genre.get().getName());

        assertThat(genreRepository.getGenreById(9999999L)).isEmpty();
    }
}
