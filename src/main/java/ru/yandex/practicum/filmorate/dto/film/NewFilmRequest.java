package ru.yandex.practicum.filmorate.dto.film;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.films.Genre;
import ru.yandex.practicum.filmorate.model.films.Mpa;

import java.time.LocalDate;
import java.util.List;

@Data
public class NewFilmRequest {
    private int duration;
    private String name;
    private String description;
    private Mpa mpa;
    private List<Genre> genres;
    private LocalDate releaseDate;

    public boolean hasGenres() {
        return genres != null;
    }
}
