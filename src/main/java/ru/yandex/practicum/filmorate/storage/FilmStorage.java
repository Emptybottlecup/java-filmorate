package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.films.Film;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> getAllFilms();

    Optional<Film> getFilmById(Long id);

    Optional<Film> addNewFilm(Film film);

    Optional<Film> updateUserInformation(Film film);
}
