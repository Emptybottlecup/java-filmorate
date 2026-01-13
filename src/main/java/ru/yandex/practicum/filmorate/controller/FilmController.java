package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;

@Slf4j
@RequestMapping("/films")
@RestController
public class FilmController {
    private ArrayList<Film> films;

    @GetMapping
    ArrayList<Film> getAllFilms() {
        return films;
    }

    @PostMapping
    Film addFilm(@RequestBody Film film) {
        try {
            validateFilm(film);
            films.add(film);
            log.debug("Был добавлен новый фильм {}", film.getName());
            return film;
        } catch (ValidationException e) {
            log.warn(e.getMessage());
        }
        return null;
    }

    @PutMapping
    Film putFilm(@RequestBody Film newFilm) {
        try {
            validateFilm(newFilm);
            for(int i = 0; i < films.size(); ++i) {
                if(films.get(i).getName().equals(newFilm.getName())) {
                    films.set(i,newFilm);
                    log.debug("Данный фильма {} были обновлены", newFilm.getName());
                    return newFilm;
                }
            }
            films.add(newFilm);
            log.debug("Был добавлен новый пользователь {}", newFilm.getName());
            return newFilm;
        } catch (ValidationException e) {
            log.warn(e.getMessage());
        }
        return null;
    }

    void validateFilm(Film film) {
        if(film.getName().isEmpty() || film.getName().length() <= 200) {
            throw new ValidationException("Неверно указано название фильма");
        } else if(film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Неверно указана дата релиза фильма");
        } else if(film.getDuration() < 0) {
            throw new ValidationException("Неверно указана продолжительности фильма");
        }
    }
}
