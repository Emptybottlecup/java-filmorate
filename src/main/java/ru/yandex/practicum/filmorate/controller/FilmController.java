package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundId;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RequestMapping("/films")
@RestController
public class FilmController {
    private int lastId = 1;
    private final HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping
    List<Film> getAllFilms() {
        return films.values().stream().toList();
    }

    @PostMapping
    Film addFilm(@Valid @RequestBody Film film) {
        try {
            validateFilm(film);
            film.setId(lastId);
            films.put(lastId, film);
            ++lastId;
            log.debug("Был добавлен новый фильм {}", film.getName());
            return film;
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping
    Film putFilm(@Valid @RequestBody Film newFilm) {
        try {
            validateFilm(newFilm);
            checkFilmId(newFilm);
            films.put(newFilm.getId(), newFilm);
            log.debug("Данные фильма {} были обновлены", newFilm.getName());
            return newFilm;
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NotFoundId e) {
            log.warn(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    public static void validateFilm(Film film) throws ValidationException {
        if (film.getName().isEmpty()) {
            throw new ValidationException("Неверно указано название фильма");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Неверно указано описание фильма");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Неверно указана дата релиза фильма");
        } else if (film.getDuration() < 0) {
            throw new ValidationException("Неверно указана продолжительности фильма");
        }
    }

    void checkFilmId(Film film) throws NotFoundId {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundId("Такого фильма нет");
        }
    }
}
