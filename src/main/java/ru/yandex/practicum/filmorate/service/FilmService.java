package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.Exceptions.NotHaveLike;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public List<Film> getPopularFilms(int count) {
        if(count < 0) {
            log.warn("Неверное количество выводимых фильмов");
            throw new ConditionsNotMetException("Отсчет не может начинаться с отрицательного числа", "count");
        }
        return filmStorage.getAllFilms().stream().sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()
                        * -1))
                .limit(count).toList();
    }

    public Film addFilm(Film film) {
        validateFilm(film);
        return filmStorage.addNewFilm(film);
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
        if(!film.getLikes().contains(userId)) {
            log.warn("У данного фильма {} не лайка от пользователя {}", film.getName(), user.getLogin());
            throw new NotHaveLike(film.getName(), user.getLogin());
        }
        if(film.getLikes().remove(user.getId())) {
            log.debug("Был удален лайк у фильма {} пользователя {}", film.getName(), user.getLogin());
        }
        return film;
    }

    public Film putFilm(Film newFilm) {
        validateFilm(newFilm);
        return filmStorage.updateFilmInformation(newFilm);
    }

    public static boolean validateFilm(Film film) throws ValidationException {
        if (film.getName().isEmpty()) {
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
        }
        return true;
    }
}
