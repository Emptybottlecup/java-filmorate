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
import ru.yandex.practicum.filmorate.dao.repository.LikesRepository;
import ru.yandex.practicum.filmorate.dao.repository.MpaRepository;
import ru.yandex.practicum.filmorate.dao.repository.UserRepository;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.films.Film;
import ru.yandex.practicum.filmorate.model.films.Mpa;
import ru.yandex.practicum.filmorate.model.users.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate.dao")
public class FilmorateDAOTestLikesRepository {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final LikesRepository likesRepository;

    @BeforeEach
    public void addFilmsAndUsers() {
        User newUser = new User();
        newUser.setLogin("andrey");
        newUser.setBirthday(LocalDate.now());
        newUser.setEmail("dobr@name.ru");
        newUser.setName("Andrey");
        userStorage.addNewUser(newUser);

        User newUser2 = new User();
        newUser2.setLogin("andrey2");
        newUser2.setBirthday(LocalDate.now());
        newUser2.setEmail("dobreed@.ru");
        newUser2.setName("Andrey2");
        userStorage.addNewUser(newUser2);

        Film film = new Film();
        film.setReleaseDate(LocalDate.now());
        film.setName("HarryPotter");
        film.setDuration(100);
        film.setIdMpa(1);
        film.setDescription("efefefeeffeefee");
        filmStorage.addNewFilm(film);
    }

    @Test
    public void addLike() {
        List<User> users = userStorage.getAllUsers();
        List<Film> films = filmStorage.getAllFilms();

        Optional<Like> like1 = likesRepository.addLike(films.get(0).getId(), users.get(0).getId());
        assertThat(like1).isPresent();

        Optional<Like> like2 = likesRepository.addLike(films.get(0).getId(), users.get(1).getId());
        assertThat(like2).isPresent();

        List<Like> likes = likesRepository.getLikesOfFilm(films.get(0).getId());
        assertEquals(2, likes.size());


        assertThrows(DataIntegrityViolationException.class, () -> likesRepository.addLike(9999999L, users.get(0)
                .getId()));

        assertThrows(DataIntegrityViolationException.class, () -> likesRepository.addLike(films.get(0).getId(),
                99999L));

        assertThrows(DuplicateKeyException.class, () -> likesRepository.addLike(films.get(0)
                .getId(), users.get(1).getId()));
    }

    @Test
    public void getLikesOfFilm() {
        List<User> users = userStorage.getAllUsers();
        List<Film> films = filmStorage.getAllFilms();

        likesRepository.addLike(films.get(0).getId(), users.get(0).getId());
        likesRepository.addLike(films.get(0).getId(), users.get(1).getId());
        List<Like> likes = likesRepository.getLikesOfFilm(films.get(0).getId());

        assertEquals(2, likes.size());
    }

    @Test
    public void deleteLike() {
        List<User> users = userStorage.getAllUsers();
        List<Film> films = filmStorage.getAllFilms();
        List<Like> likes1 = likesRepository.getLikesOfFilm(films.get(0).getId());
        assertEquals(0, likes1.size());

        likesRepository.addLike(films.get(0).getId(), users.get(0).getId());
        likesRepository.addLike(films.get(0).getId(), users.get(1).getId());
        List<Like> likes2 = likesRepository.getLikesOfFilm(films.get(0).getId());
        assertEquals(2, likes2.size());

        assertThat(likesRepository.deleteLike(films.get(0).getId(), users.get(0).getId())).isTrue();
        List<Like> likes3 = likesRepository.getLikesOfFilm(films.get(0).getId());
        assertEquals(1, likes3.size());

        assertThat(likesRepository.deleteLike(99999999999L, users.get(0).getId())).isFalse();
        assertThat(likesRepository.deleteLike(films.get(0).getId(), 999999999L)).isFalse();
    }
}
