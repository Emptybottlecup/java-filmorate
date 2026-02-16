package ru.yandex.practicum.filmorate.dto.film;

import lombok.Data;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.mpa.MpaDto;

import java.time.LocalDate;
import java.util.List;

@Data
public class FilmDto {
    private Long id;
    private int duration;
    private String name;
    private MpaDto mpa;
    private String description;
    private List<GenreDto> genres;
    private LocalDate releaseDate;
}
