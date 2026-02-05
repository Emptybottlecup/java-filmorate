package ru.yandex.practicum.filmorate.dto.film;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.films.Genre;
import ru.yandex.practicum.filmorate.model.films.Mpa;

import java.time.LocalDate;
import java.util.List;

@Data
public class FilmUpdateInformation {
    private Long id;
    private Integer duration;
    private String name;
    private String description;
    private Mpa mpa;
    private List<Genre> genres;
    private LocalDate releaseDate;


    public boolean hasId() {
        return id != null;
    }

    public boolean hasDuration() {
        return duration != null;
    }

    public boolean hasName() {
        return name != null && !name.isBlank();
    }

    public boolean hasDescription() {
        return description != null && !description.isBlank();
    }

    public boolean hasMpa(){
        return mpa != null;
    }

    public boolean hasGenres() {
        return genres != null;
    }

    public boolean hasReleaseDate() {
        return releaseDate != null;
    }
}
