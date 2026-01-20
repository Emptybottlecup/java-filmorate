package ru.yandex.practicum.filmorate.Exceptions.NotFoundId;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotFoundId extends RuntimeException {
    private final Long id;
    private final FilmOrUser object;
}
