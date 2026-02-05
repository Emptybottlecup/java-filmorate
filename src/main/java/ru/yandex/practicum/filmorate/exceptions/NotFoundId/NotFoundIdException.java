package ru.yandex.practicum.filmorate.exceptions.NotFoundId;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotFoundIdException extends RuntimeException {
    private final Long id;
    private final WhichObjectNotFound object;
}
