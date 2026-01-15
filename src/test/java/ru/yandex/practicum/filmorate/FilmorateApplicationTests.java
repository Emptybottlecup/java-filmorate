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
        Film validFilm1 = new Film("Harry Potter", "cool film", LocalDate.of(2001, 1,
                1), 120);
        Film invalidFilmName = new Film("", "cool film", LocalDate.of(2001, 1,
                1), 120);

        Film validFilm2 = new Film("Harry Potter", "111111111111111111111111111111111111111111111111111" +
                "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "1111111111111111111111111111111111111111111111", LocalDate.of(2001, 1, 1),
                120);
        Film invalidFilDescription = new Film("Harry Potter", "1111111111111111111111111111111111111111" +
                "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "11111111111111111111111111111111111111111111111111111111111", LocalDate.of(2001, 01,
                1), 120);

        Film validFilm3 = new Film("Harry Potter", "cool film", LocalDate.of(1895, 12,
                28), 120);
        Film invalidFilmReleaseDate = new Film("Harry Potter", "cool film", LocalDate.of(1895,
                12, 27), 120);

        Film validFilm4 = new Film("Harry Potter", "cool film", LocalDate.of(1895, 12,
                28), 1);
        Film invalidFilmDuration = new Film("Harry Potter", "cool film", LocalDate.of(2001,
                1, 1), -1);

        try {
            Assertions.assertTrue(FilmController.validateFilm(validFilm1));
            FilmController.validateFilm(invalidFilmName);
        } catch (ValidationException e) {
            Assertions.assertEquals("Неверно указано название фильма", e.getMessage());
        }
        try {
            Assertions.assertTrue(FilmController.validateFilm(validFilm2));
            FilmController.validateFilm(invalidFilDescription);
        } catch (ValidationException e) {
            Assertions.assertEquals("Неверно указано описание фильма", e.getMessage());
        }
        try {
            Assertions.assertTrue(FilmController.validateFilm(validFilm3));
            FilmController.validateFilm(invalidFilmReleaseDate);
        } catch (ValidationException e) {
            Assertions.assertEquals("Неверно указана дата релиза фильма", e.getMessage());
        }
        try {
            Assertions.assertTrue(FilmController.validateFilm(validFilm4));
            FilmController.validateFilm(invalidFilmDuration);
        } catch (ValidationException e) {
            Assertions.assertEquals("Неверно указана продолжительности фильма", e.getMessage());
        }
    }

    void testUsers() {
        User validUser1 = new User("fff3@mail.ru", "dfdf", LocalDate.of(1980, 12, 1));
        User invalidUserEmail1 = new User("fff 3", "dfdf", LocalDate.of(1980, 12, 1));
        User invalidUserEmail2 = new User("", "dfdf", LocalDate.of(1980, 12, 1));

        User invalidUserLogin1 = new User("fff@mail.ru", "", LocalDate.of(1980, 12, 1));
        User invalidUserLogin2 = new User("fff@mail.ru", "dsfdfs dfsf", LocalDate.of(1980, 12, 1));

        User validUser2 = new User("fff3@mail.ru", "dfdf", LocalDate.now());
        User validUser3 = new User("fff3@mail.ru", "dfdf", LocalDate.of(LocalDate.now().getYear(),LocalDate
                .now().getMonth(), LocalDate.now().getDayOfMonth() - 1));
        User invalidUserBirthday1  = new User("fff3@mail.ru", "dfdf", LocalDate.of(LocalDate.now().getYear(),LocalDate
                .now().getMonth(), LocalDate.now().getDayOfMonth() + 1));
        User invalidUserBirthday2 = new User("fff@mail.ru", "dfdf", LocalDate.of(2500, 12, 1));

        try {
            Assertions.assertTrue(UserController.validateUser(validUser1));
            UserController.validateUser(invalidUserEmail1);
        } catch (ValidationException e) {
            Assertions.assertEquals("Неверно указан email", e.getMessage());
        }
        try {
            UserController.validateUser(invalidUserEmail2);
        } catch (ValidationException e) {
            Assertions.assertEquals("Неверно указан email", e.getMessage());
        }
        try {
            UserController.validateUser(invalidUserLogin1);
        } catch (ValidationException e) {
            Assertions.assertEquals("Неверно указан логин", e.getMessage());
        }
        try {
            UserController.validateUser(invalidUserLogin2);
        } catch (ValidationException e) {
            Assertions.assertEquals("Неверно указан логин", e.getMessage());
        }
        try {
            Assertions.assertTrue(UserController.validateUser(validUser2));
            Assertions.assertTrue(UserController.validateUser(validUser3));
            UserController.validateUser(invalidUserBirthday1);
        } catch (ValidationException e) {
            Assertions.assertEquals("Неверно указана дата рождения", e.getMessage());
        }
        try {
            UserController.validateUser(invalidUserBirthday2);
        } catch (ValidationException e) {
            Assertions.assertEquals("Неверно указана дата рождения", e.getMessage());
        }
    }

}
