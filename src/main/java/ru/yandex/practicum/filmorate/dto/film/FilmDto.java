package ru.yandex.practicum.filmorate.dto.film;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.films.Genre;
import ru.yandex.practicum.filmorate.model.films.Mpa;

import java.time.LocalDate;
import java.util.List;

@Data
public class FilmDto {
    private Long id;
    private int duration;
    private String name;
    private Mpa mpa;
    private String description;
    private List<Genre> genres;
    private LocalDate releaseDate;
}
