package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;

@Slf4j
@RequestMapping("/users")
@RestController
public class UserController {
    private ArrayList<User> users;

    @GetMapping
    ArrayList<User> getAllUsers() {
        return users;
    }

    @PostMapping
    User addUser(@RequestBody User user) {
        try {
            validateUser(user);
            users.add(user);
            log.debug("Был добавлен новый пользователь {}", user.getLogin());
            return user;
        } catch (ValidationException e) {
            log.warn(e.getMessage());
        }
        return null;
    }

    @PutMapping
    User putUser(@RequestBody User newUser) {
        try {
            validateUser(newUser);
            for(int i = 0; i < users.size(); ++i) {
                if(users.get(i).getEmail().equals(newUser.getEmail())) {
                    users.set(i,newUser);
                    log.debug("Данный пользователя {} были обновлены", newUser.getLogin());
                    return newUser;
                }
            }
            users.add(newUser);
            log.debug("Был добавлен новый пользователь {}", newUser.getLogin());
            return newUser;
        } catch (ValidationException e) {
            log.warn(e.getMessage());
        }
        return null;
    }

    void validateUser(User user) {
        if(user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Неверно указан email");
        } else if(user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Неверно указан логин");
        } else if(user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Неверно указана дата рождения");
        }
    }
}
