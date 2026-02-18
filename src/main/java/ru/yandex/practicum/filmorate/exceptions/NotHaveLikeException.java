package ru.yandex.practicum.filmorate.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotHaveLikeException extends RuntimeException {
    private final String filmName;
    private final String userLogin;
}
