package ru.yandex.practicum.filmorate.mappers;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.genre.GenreAndFilmDto;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.genre.NewGenreRequest;
import ru.yandex.practicum.filmorate.model.films.Genre;
import ru.yandex.practicum.filmorate.model.films.GenreAndFilm;

@UtilityClass
public class GenreMapper {
    public static GenreDto mapToGenreDto(Genre genre) {
        GenreDto genreDto = new GenreDto();

        genreDto.setId(genre.getId());
        genreDto.setName(genre.getName());

        return genreDto;
    }

    public static GenreAndFilmDto mapToGenreAndFilmDto(GenreAndFilm genreAndFilm) {
        GenreAndFilmDto genreAndFilmDto = new GenreAndFilmDto();

        genreAndFilmDto.setIdFilm(genreAndFilm.getIdFilm());
        genreAndFilmDto.setIdGenre(genreAndFilm.getIdGenre());

        return genreAndFilmDto;
    }
}
