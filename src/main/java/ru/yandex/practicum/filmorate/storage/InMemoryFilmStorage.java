package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundId.FilmOrUser;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundId.NotFoundId;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private Long lastId = 1L;
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public ArrayList<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(Long id) {
        if(!films.containsKey(id)) {
            log.warn("Id {} фильма не был найден", id);
            throw new NotFoundId(id, FilmOrUser.FILM);
        }
        return films.get(id);
    }

    @Override
    public Film addNewFilm(Film film) {
        film.setId(lastId);
        films.put(lastId, film);
        ++lastId;
        log.debug("Был добавлен новый фильм {}", film.getName());
        return film;
    }

    @Override
    public Film updateFilmInformation(Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Id {} фильма не был найден", film.getId());
            throw new NotFoundId(film.getId(), FilmOrUser.FILM);
        }
        films.put(film.getId(), film);
        log.debug("Данные фильма {} были обновлены", film.getName());
        return film;
    }
}
