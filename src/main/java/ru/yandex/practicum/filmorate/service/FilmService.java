package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.FilmUpdateInformation;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundId.NotFoundIdException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundId.WhichObjectNotFound;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.films.Film;
import ru.yandex.practicum.filmorate.model.films.Genre;
import ru.yandex.practicum.filmorate.model.films.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final MpaService mpaService;
    private final GenreService genreService;
    private final UserService userService;
    private final LikeService likeService;

    public List<FilmDto> getAllFilms() {
        return filmStorage.getAllFilms()
                .stream()
                .map(film -> {
                    List<Genre> genres = genreService.getGenreOfFilmById(film.getId());
                    Mpa mpa = mpaService.getMpaById(film.getIdMpa());
                    return FilmMapper.mapToFilmDto(film, mpa, genres);
                })
                .toList();
    }

    public FilmDto getFilmById(long id) {
        Film film = filmStorage.getFilmById(id)
                .orElseThrow(() -> new NotFoundIdException(id, WhichObjectNotFound.FILM));

        Mpa mpa = mpaService.getMpaById(film.getIdMpa());

        List<Genre> genres = genreService.getGenreOfFilmById(film.getId());

        return FilmMapper.mapToFilmDto(film, mpa, genres);
    }

    public FilmDto addFilm(NewFilmRequest newFilmRequest) {

        if (newFilmRequest.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильма должна быть позже 28.12.1895");
        }

        Mpa mpa = mpaService.getMpaById(newFilmRequest.getMpa().getId());

        Film film = filmStorage.addNewFilm(FilmMapper.createFilm(newFilmRequest))
                .orElseThrow(() -> new InternalServerException("Не получилось создать фильм"));


        List<Genre> genres = genreService.addGenresOfFilm(film.getId(), newFilmRequest.getGenres());

        return FilmMapper.mapToFilmDto(film, mpa, genres);
    }

    public List<FilmDto> getPopularFilms(int count) {
        if (count < 0) {
            log.warn("Неверное количество выводимых фильмов");
            throw new ConditionsNotMetException("Отсчет не может начинаться с отрицательного числа", "count");
        }
        return getAllFilms().stream()
                .sorted(Comparator.comparingInt((filmDto) -> likeService.getLikesOfFilm(filmDto.getId())
                        .size() * -1))
                .limit(count).toList();
    }

    public void deleteLike(Long filmId, Long userId) {
        FilmDto filmDto = getFilmById(filmId);
        UserDto userDto = userService.getUser(userId);

        likeService.deleteLike(filmId, userId);

        log.debug("Был удален лайк у фильма {} пользователя {}", filmDto.getName(), userDto.getLogin());
    }

    public Like addLike(Long filmId, Long userId) {
        FilmDto filmDto = getFilmById(filmId);
        UserDto user = userService.getUser(userId);
        Like like = likeService.addLike(filmId, userId);
        log.debug("Был добавлен лайк фильму {} пользователем {}", filmDto.getName(), user.getLogin());
        return like;
    }


    public FilmDto putFilm(FilmUpdateInformation filmUpdateInformation) {
        if (filmUpdateInformation.hasMpa()) {
            mpaService.getMpaById(filmUpdateInformation.getMpa().getId());
        }

        Film film = filmStorage.getFilmById(filmUpdateInformation.getId())
                .orElseThrow(() -> new NotFoundIdException(filmUpdateInformation.getId(), WhichObjectNotFound.FILM));


        Mpa mpa = mpaService.getMpaById(film.getIdMpa());

        List<Genre> genres;
        if (filmUpdateInformation.hasGenres()) {
            genreService.deleteGenreOfFilm(film.getId());
            genres = genreService.addGenresOfFilm(film.getId(), filmUpdateInformation.getGenres());
        } else {
            genres = genreService.getGenreOfFilmById(film.getId());
        }

        FilmMapper.updateFilmInformation(film, filmUpdateInformation);

        filmStorage.updateFilmInformation(film).orElseThrow(() ->
                new InternalServerException("Не получилось обновить данные фильма"));


        return FilmMapper.mapToFilmDto(film, mpa, genres);
    }
}
