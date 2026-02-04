package ru.yandex.practicum.filmorate.model.films;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Film {
    private Long id;
    private int duration;
    private String name;
    private String description;
    private long idMpa;
    private LocalDate releaseDate;
}