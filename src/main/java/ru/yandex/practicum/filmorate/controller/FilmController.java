package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.FilmUpdateInformation;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.model.films.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RequestMapping("/films")
@RestController
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PostMapping
    public FilmDto addFilm(@RequestBody NewFilmRequest newFilm) {
        return filmService.addFilm(newFilm);
    }

    @PutMapping
    public FilmDto putFilm(@RequestBody FilmUpdateInformation newFilm) {
        return filmService.putFilm(newFilm);
    }

    @GetMapping("/{id}")
    public FilmDto getFilmById(@PathVariable long id) {
        return filmService.getFilmById(id);
    }

    /*
    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.deleteLike(id, userId);
    }
    */
}
