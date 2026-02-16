package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.repository.GenreRepository;
import ru.yandex.practicum.filmorate.dao.repository.GenresAndFilmRepository;
import ru.yandex.practicum.filmorate.dto.genre.GenreAndFilmDto;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.genre.NewGenreRequest;
import ru.yandex.practicum.filmorate.exceptions.NotFoundId.NotFoundIdException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundId.WhichObjectNotFound;
import ru.yandex.practicum.filmorate.mappers.GenreMapper;
import ru.yandex.practicum.filmorate.model.films.Genre;
import ru.yandex.practicum.filmorate.model.films.GenreAndFilm;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;
    private final GenresAndFilmRepository genresAndFilmRepository;

    public List<GenreDto> getGenreOfFilmById(long filmId) {
        List<GenreAndFilm> genresOfFilm = genresAndFilmRepository.getAllGenreOfFilm(filmId);

        Map<Long, GenreDto> genreIdAndGenreDto = getAllGenres().stream()
                .collect(Collectors.toMap(GenreDto::getId, genreDto -> genreDto));

        return genresOfFilm.stream().map(genreAndFilm -> genreIdAndGenreDto.get(genreAndFilm.getIdGenre()))
                .toList();
    }

    public List<GenreAndFilmDto> getAllGenresAndFilms() {
        return genresAndFilmRepository.getAllGenresAndFilms().stream()
                .map(GenreMapper::mapToGenreAndFilmDto)
                .toList();
    }

    public List<GenreDto> getAllGenres() {
        return genreRepository.getAllGenres().stream()
                .map(GenreMapper::mapToGenreDto)
                .toList();
    }

    public GenreDto getGenreById(long id) {
        Genre genre = genreRepository.getGenreById(id).orElseThrow(() -> new NotFoundIdException(id, WhichObjectNotFound.GENRE));
        return GenreMapper.mapToGenreDto(genre);
    }

    public List<GenreDto> addGenresOfFilm(long filmId, List<NewGenreRequest> newGenreRequests) {
        Set<Long> setId = new HashSet<>();
        List<GenreDto> genresToReturn = new ArrayList<>();
        if (newGenreRequests != null) {
            for (NewGenreRequest newGenreRequest : newGenreRequests) {
                if (!setId.contains(newGenreRequest.getId())) {
                    long genreId = newGenreRequest.getId();
                    Genre genreToAdd = genreRepository.getGenreById(genreId).orElseThrow(() -> new NotFoundIdException(genreId, WhichObjectNotFound.GENRE));

                    if (genresAndFilmRepository.insertGenreAndFilm(filmId, genreId).isPresent()) {
                        setId.add(genreId);
                        genresToReturn.add(GenreMapper.mapToGenreDto(genreToAdd));
                    }
                }
            }
        }
        return genresToReturn;
    }

    public void deleteGenreOfFilm(long filmId) {
        genresAndFilmRepository.deleteAllGenresOfFilmById(filmId);
    }

}
