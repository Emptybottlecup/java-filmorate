package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.NotFound;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.repository.FilmRepository;
import ru.yandex.practicum.filmorate.dao.repository.GenreRepository;
import ru.yandex.practicum.filmorate.dao.repository.GenresAndFilmRepository;
import ru.yandex.practicum.filmorate.dao.repository.MpaRepository;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.FilmUpdateInformation;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundId.NotFoundIdException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundId.WhichObjectNotFound;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.films.Film;
import ru.yandex.practicum.filmorate.model.films.Genre;
import ru.yandex.practicum.filmorate.model.films.GenreAndFilm;
import ru.yandex.practicum.filmorate.model.films.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final MpaRepository mpaRepository;
    private final GenreAndFilmService genreAndFilmService;

    public List<FilmDto> getAllFilms() {
        return filmStorage.getAllFilms()
                .stream()
                .map(film ->{
                    List<Genre> genres = genreAndFilmService.getGenreOfFilmById(film.getId());
                    Mpa mpa = mpaRepository.getMpaById(film.getId()).get();
                    return FilmMapper.mapToFilmDto(film, mpa, genres);
                })
                .toList();
    }

    public FilmDto getFilmById(long id) {
        Film film = filmStorage.getFilmById(id)
                .orElseThrow(() -> new NotFoundIdException(id, WhichObjectNotFound.FILM));

        Mpa mpa = mpaRepository.getMpaById(film.getIdMpa()).get();

        List<Genre> genres = genreAndFilmService.getGenreOfFilmById(film.getId());

        return FilmMapper.mapToFilmDto(film, mpa, genres);
    }

    public FilmDto addFilm(NewFilmRequest newFilmRequest) {
        validateFilm(newFilmRequest);

        Mpa mpa = mpaRepository.getMpaById(newFilmRequest.getMpa().getId())
                .orElseThrow(() -> new NotFoundIdException(newFilmRequest.getMpa().getId(), WhichObjectNotFound.MPA));

        Film film = filmStorage.addNewFilm(FilmMapper.createFilm(newFilmRequest))
                .orElseThrow(() -> new InternalServerException("Не получилось создать фильм"));

        List<Genre> genres = genreAndFilmService.addGenresOfFilm(film.getId(), newFilmRequest.getGenres());

        return FilmMapper.mapToFilmDto(film, mpa, genres);
    }

    /*public List<Film> getPopularFilms(int count) {
        if (count < 0) {
            log.warn("Неверное количество выводимых фильмов");
            throw new ConditionsNotMetException("Отсчет не может начинаться с отрицательного числа", "count");
        }
        return filmStorage.getAllFilms().stream().sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()
                        * -1))
                .limit(count).toList();
    }

    public Film addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilm(filmId);
        User user = userService.getUser(userId);
        film.getLikes().add(user.getId());
        log.debug("Был добавлен лайк фильму {} пользователем {}", film.getName(), user.getLogin());
        return film;
    }

    public Film deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilm(filmId);
        User user = userService.getUser(userId);
        if (!film.getLikes().contains(userId)) {
            log.warn("У данного фильма {} не лайка от пользователя {}", film.getName(), user.getLogin());
            throw new NotHaveLikeException(film.getName(), user.getLogin());
        }
        if (film.getLikes().remove(user.getId())) {
            log.debug("Был удален лайк у фильма {} пользователя {}", film.getName(), user.getLogin());
        }
        return film;
    }
    */

    public FilmDto putFilm(FilmUpdateInformation filmUpdateInformation) {
        if(!filmUpdateInformation.hasId()) {
            throw new ValidationException("Для обновления данных фильма необходимо передать его id");
        }
        if(filmUpdateInformation.hasMpa()) {
            mpaRepository.getMpaById(filmUpdateInformation.getId())
                    .orElseThrow(() -> new NotFoundIdException(filmUpdateInformation.getMpa().getId(),
                            WhichObjectNotFound.MPA));
        }

        Film film = filmStorage.getFilmById(filmUpdateInformation.getId())
                .orElseThrow(() -> new NotFoundIdException(filmUpdateInformation.getId(), WhichObjectNotFound.FILM));


        Mpa mpa = mpaRepository.getMpaById(film.getId()).get();
        List<Genre> genres = new ArrayList<>();
        if(filmUpdateInformation.hasGenres()) {
            genreAndFilmService.deleteGenreOfFilm(film.getId());
            genres = genreAndFilmService.addGenresOfFilm(film.getId(), filmUpdateInformation.getGenres());
        } else {
            genres = genreAndFilmService.getGenreOfFilmById(film.getId());
        }

        FilmMapper.updateFilmInformation(film, filmUpdateInformation);

        filmStorage.updateUserInformation(film).orElseThrow(() ->
                new InternalServerException("Не получилось обновить данные фильма"));


        return FilmMapper.mapToFilmDto(film, mpa, genres);
    }

    private void validateFilm(NewFilmRequest film) {
        if (film.getName() == null || film.getName().isEmpty() || film.getName().isBlank()) {
            log.warn("Ошибка валидации: Название фильма не может быть пустым");
            throw new ValidationException("Неверно указано название фильма.");
        } else if (film.getDescription().length() > 200) {
            log.warn("Ошибка валидации: Описание фильма не может состоять больше чем из 200 символов");
            throw new ValidationException("Неверно указано описание фильма.");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Ошибка валидации: Дата релиза фильма не может быть раньше {}", LocalDate.of(1895, 12, 28));
            throw new ValidationException("Неверно указана дата релиза фильма.");
        } else if (film.getDuration() < 1) {
            log.warn("Ошибка валидации: Продолжительность фильма не может быть меньше 1");
            throw new ValidationException("Неверно указана продолжительности фильма.");
        } else if (film.getMpa() == null) {
            log.warn("Ошибка валидации: Mpa не был указан");
            throw new ValidationException("Неверно указан Mpa.");
        }
    }
}
