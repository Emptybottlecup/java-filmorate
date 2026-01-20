package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;

public interface FilmStorage {
    ArrayList<Film> getAllFilms();

    Film getFilm(Long id);

    Film addNewFilm(Film film);

    Film updateFilmInformation(Film film);
}
