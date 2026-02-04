package ru.yandex.practicum.filmorate.exceptions;

import lombok.Getter;

@Getter
public class ConditionsNotMetException extends RuntimeException {
    private final String type;

    public ConditionsNotMetException(String message, String type) {
        super(message);
        this.type = type;
    }
}
