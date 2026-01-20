package ru.yandex.practicum.filmorate.Exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotHaveLike extends RuntimeException {
    private final String filmName;
    private final String userLogin;
}
