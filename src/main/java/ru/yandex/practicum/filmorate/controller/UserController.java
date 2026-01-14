package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundId;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RequestMapping("/users")
@RestController
public class UserController {
    private int lastId = 1;
    private final HashMap<Integer, User> users = new HashMap<>();

    @GetMapping
    List<User> getAllUsers() {
        return users.values().stream().toList();
    }

    @PostMapping
    User addUser(@Valid @RequestBody User user) {
        try {
            validateUser(user);
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            user.setId(lastId);
            users.put(lastId, user);
            ++lastId;
            log.debug("Был добавлен новый пользователь {}", user.getLogin());
            return user;
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping
    User putUser(@Valid @RequestBody User newUser) {
        try {
            validateUser(newUser);
            checkUserId(newUser);
            users.put(newUser.getId(), newUser);
            log.debug("Данные пользователя {} были обновлены", newUser.getLogin());
            return newUser;
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NotFoundId e) {
            log.warn(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    public static void validateUser(User user) throws ValidationException {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Неверно указан email");
        } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Неверно указан логин");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Неверно указана дата рождения");
        }
    }

    void checkUserId(User user) throws NotFoundId {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundId("Такого пользователя нет");
        }
    }
}