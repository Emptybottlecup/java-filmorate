package ru.yandex.practicum.filmorate.mappers;

import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.FilmUpdateInformation;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.model.films.Film;
import ru.yandex.practicum.filmorate.model.films.Genre;
import ru.yandex.practicum.filmorate.model.films.Mpa;

import java.util.List;

public class FilmMapper {

    public static FilmDto mapToFilmDto(Film film, Mpa mpa, List<Genre> genres) {
        FilmDto filmDto = new FilmDto();

        filmDto.setId(film.getId());
        filmDto.setName(film.getName());
        filmDto.setDuration(film.getDuration());
        filmDto.setReleaseDate(film.getReleaseDate());
        filmDto.setMpa(mpa);
        filmDto.setGenres(genres);
        filmDto.setDescription(film.getDescription());

        return filmDto;
    }

     public static Film createFilm(NewFilmRequest newFilm) {
        Film film = new Film();

        film.setDuration(newFilm.getDuration());
        film.setReleaseDate(newFilm.getReleaseDate());
        film.setName(newFilm.getName());
        film.setDescription(newFilm.getDescription());
        film.setIdMpa(newFilm.getMpa().getId());

        return film;
    }

    public static Film updateFilmInformation(Film film, FilmUpdateInformation filmUpdateInformation) {
        if (filmUpdateInformation.hasDescription()) {
            film.setDescription(filmUpdateInformation.getDescription());
        }
        if (filmUpdateInformation.hasMpa()) {
            film.setIdMpa(filmUpdateInformation.getMpa().getId());
        }
        if (filmUpdateInformation.hasDuration()) {
            film.setDuration(filmUpdateInformation.getDuration());
        }
        if (filmUpdateInformation.hasName()) {
            film.setName(filmUpdateInformation.getName());
        }
        if (filmUpdateInformation.hasReleaseDate()) {
            film.setReleaseDate(filmUpdateInformation.getReleaseDate());
        }
        return film;
    }
}
