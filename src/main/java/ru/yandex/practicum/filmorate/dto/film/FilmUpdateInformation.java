package ru.yandex.practicum.filmorate.dto.film;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.dto.genre.NewGenreRequest;
import ru.yandex.practicum.filmorate.dto.mpa.NewMpaRequest;

import java.time.LocalDate;
import java.util.List;

@Data
public class FilmUpdateInformation {
    @NotNull
    @Positive
    private Long id;
    @Positive
    private Integer duration;
    private String name;
    @Size(max = 200)
    private String description;
    private NewMpaRequest mpa;
    private List<NewGenreRequest> genres;
    @PastOrPresent
    private LocalDate releaseDate;

    public boolean hasDuration() {
        return duration != null;
    }

    public boolean hasName() {
        return name != null && !name.isBlank();
    }

    public boolean hasDescription() {
        return description != null && !description.isBlank();
    }

    public boolean hasMpa() {
        return mpa != null;
    }

    public boolean hasGenres() {
        return genres != null;
    }

    public boolean hasReleaseDate() {
        return releaseDate != null;
    }
}
