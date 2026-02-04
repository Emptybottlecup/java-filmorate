package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundId.FilmOrUser;
import ru.yandex.practicum.filmorate.exceptions.NotFoundId.NotFoundIdException;
import ru.yandex.practicum.filmorate.exceptions.NotFriendsException;
import ru.yandex.practicum.filmorate.exceptions.NotHaveLikeException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerNotFoundId(final NotFoundIdException e) {
        String object = "Пользователя";
        if (e.getObject().equals(FilmOrUser.FILM)) {
            object = "Фильма";
        }
        return new ErrorResponse("Неверный id.", object + " с id " + e.getId() + " не существует.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerNotFriendsException(final NotFriendsException e) {
        return new ErrorResponse("Не получилось найти дружескую связь", String.format("Пользователь с id = %d не " +
                "является другом пользователя с id = %d", e.getIdUser(), e.getIdUserFriend()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerValidationException(final ValidationException e) {
        return new ErrorResponse("Ошибка валидации.", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerNotHaveLike(final NotHaveLikeException e) {
        return new ErrorResponse("Фильм не содержит лайк пользователя", "Фильм " + e.getFilmName() + " не " +
                "содержит лайк от пользователя " + e.getUserLogin() + ".");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerConditionsNotMetException(final ConditionsNotMetException e) {
        return new ErrorResponse(e.getType(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handlerThrowableException(final Throwable e) {
        return new ErrorResponse("Ошибка", e.getMessage());
    }

}
