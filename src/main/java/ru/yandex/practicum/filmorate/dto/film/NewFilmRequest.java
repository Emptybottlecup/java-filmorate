package ru.yandex.practicum.filmorate.dto.film;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.films.Genre;
import ru.yandex.practicum.filmorate.model.films.Mpa;

import java.time.LocalDate;
import java.util.List;

@Data
public class NewFilmRequest {
    @Positive
    private int duration;
    @NotBlank
    private String name;
    @NotBlank
    @Size(max = 200)
    private String description;
    @NotNull
    private Mpa mpa;
    private List<Genre> genres;
    @PastOrPresent
    private LocalDate releaseDate;
}
