package ru.yandex.practicum.filmorate.dto.film;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.dto.genre.NewGenreRequest;
import ru.yandex.practicum.filmorate.dto.mpa.NewMpaRequest;

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
    private NewMpaRequest mpa;
    private List<NewGenreRequest> genres;
    @PastOrPresent
    private LocalDate releaseDate;
}
