package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.repository.GenreRepository;
import ru.yandex.practicum.filmorate.dao.repository.GenresAndFilmRepository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundId.NotFoundIdException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundId.WhichObjectNotFound;
import ru.yandex.practicum.filmorate.model.films.Genre;
import ru.yandex.practicum.filmorate.model.films.GenreAndFilm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;
    private final GenresAndFilmRepository genresAndFilmRepository;

    public List<Genre> getGenreOfFilmById(long filmId) {
        List<GenreAndFilm> genresOfFilm = genresAndFilmRepository.getAllGenreOfFilm(filmId);
        List<Genre> genres = new ArrayList<>();

        for (GenreAndFilm genreAndFilm : genresOfFilm) {
            long genreId = genreAndFilm.getIdGenre();
            Genre genre = genreRepository.getGenreById(genreId).get();
            genres.add(genre);
        }

        return genres;
    }

    public List<Genre> getAllGenres() {
        return genreRepository.getAllGenres();
    }

    public Genre getGenreById(long id) {
        return genreRepository.getGenreById(id).orElseThrow(() -> new NotFoundIdException(id, WhichObjectNotFound.GENRE));
    }

    public List<Genre> addGenresOfFilm(long filmId, List<Genre> genres) {
        Set<Long> setId = new HashSet<>();
        List<Genre> genresToReturn = new ArrayList<>();
        if (genres != null) {
            for (Genre genre : genres) {
                if (!setId.contains(genre.getId())) {
                    long genreId = genre.getId();
                    Genre genreToAdd = genreRepository.getGenreById(genreId).orElseThrow(() -> new NotFoundIdException(genreId, WhichObjectNotFound.GENRE));

                    if (genresAndFilmRepository.insertGenreAndFilm(filmId, genreId).isPresent()) {
                        setId.add(genreId);
                        genresToReturn.add(genreToAdd);
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
