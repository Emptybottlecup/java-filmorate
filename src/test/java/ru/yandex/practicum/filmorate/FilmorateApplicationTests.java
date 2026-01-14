package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@SpringBootTest
class FilmorateApplicationTests {

    @Test
    void contextLoads() {
        testFilms();
        testUsers();
    }

    void testFilms() {
        Film invalidFilmName = new Film("", "cool film", LocalDate.of(2001, 1, 1), 120);
        Film invalidFilDescription = new Film("Harry Potter", "ehfegfehfehfejfhehfwkjfhsdjkfsjdhfskjd" +
                "fh sdjfhsdjkfsdjkfhsdkfdsfsdfsdfsdfsdffffffffffffffffffffffffffffffffffffsfsdfsdfsdfsdfsdfsdfsdfdsfs" +
                "dfsdfsdfsdfsdfs dfdfsfsdfsdf sdfsdfsdfdsfsdfdsfdsfsfdsfsdfsdfsdfsdsdfsdfsdfsdfsdfewfefefefefefefefefef",
                LocalDate.of(2001, 01, 1), 120);
        Film invalidFilmReleaseDate = new Film("Harry Potter", "cool film", LocalDate.of(1001, 1, 1), 120);
        Film invalidFilmDuration = new Film("Harry Potter", "cool film", LocalDate.of(2001, 1, 1), -100);

        try {
            FilmController.validateFilm(invalidFilmName);
        } catch (ValidationException e) {
            Assertions.assertEquals("Неверно указано название фильма", e.getMessage());
        }
        try {
            FilmController.validateFilm(invalidFilDescription);
        } catch (ValidationException e) {
            Assertions.assertEquals("Неверно указано описание фильма", e.getMessage());
        }
        try {
            FilmController.validateFilm(invalidFilmReleaseDate);
        } catch (ValidationException e) {
            Assertions.assertEquals("Неверно указана дата релиза фильма", e.getMessage());
        }
        try {
            FilmController.validateFilm(invalidFilmDuration);
        } catch (ValidationException e) {
            Assertions.assertEquals("Неверно указана продолжительности фильма", e.getMessage());
        }
    }

    void testUsers() {
        User invalidUserEmail = new User("fff 3", "dfdf", LocalDate.of(1980, 12, 1));
        User invalidUserLogin = new User("fff@mail.ru", "", LocalDate.of(1980, 12, 1));
        User invalidUserBirthday = new User("fff@mail.ru", "dfdf", LocalDate.of(2500, 12, 1));

        try {
            UserController.validateUser(invalidUserEmail);
        } catch (ValidationException e) {
            Assertions.assertEquals("Неверно указан email", e.getMessage());
        }
        try {
            UserController.validateUser(invalidUserLogin);
        } catch (ValidationException e) {
            Assertions.assertEquals("Неверно указан логин", e.getMessage());
        }
        try {
            UserController.validateUser(invalidUserBirthday);
        } catch (ValidationException e) {
            Assertions.assertEquals("Неверно указана дата рождения", e.getMessage());
        }
    }

}
