package ru.yandex.practicum.filmorate.model.films.genres;

import lombok.Data;

@Data
public class Genre {
    private long id;
    private GenreEnum genre;
}
