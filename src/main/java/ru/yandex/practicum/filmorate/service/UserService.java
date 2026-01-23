package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUser(Long id) {
        return userStorage.getUser(id);
    }

    public List<User> getCommonFriends(Long idUser, Long idOtherUser) {
        List<User> friends = new ArrayList<>();
        User user = userStorage.getUser(idUser);
        User otherUser = userStorage.getUser(idOtherUser);
        for (Long idFriend : user.getFriends()) {
            if (otherUser.getFriends().contains(idFriend)) {
                friends.add(userStorage.getUser(idFriend));
            }
        }
        return friends;
    }

    public List<User> getUserFriends(Long id) {
        List<User> friends = new ArrayList<>();
        User user = userStorage.getUser(id);
        for (Long idFriend : user.getFriends()) {
            friends.add(userStorage.getUser(idFriend));
        }
        return friends;
    }

    public User addUser(@Valid @RequestBody User user) {
        validateUser(user);
        return userStorage.addNewUser(user);
    }

    public User deleteFriends(Long idUser, Long idNewFriend) {
        User user = userStorage.getUser(idUser);
        User userFriend = userStorage.getUser(idNewFriend);
        if (user.getFriends().remove(idNewFriend) && userFriend.getFriends().remove(idUser)) {
            log.debug("Пользователи {} и {} перестали быть", user.getLogin(), userFriend.getLogin());
        }
        return user;
    }

    public User addFriends(Long idUser, Long idNewFriend) {
        User user = userStorage.getUser(idUser);
        User userFriend = userStorage.getUser(idNewFriend);
        user.getFriends().add(idNewFriend);
        userFriend.getFriends().add(idUser);
        log.debug("Пользователи {} и {} стали друзьями", user.getLogin(), userFriend.getLogin());
        return user;
    }

    public User putUser(User newUser) {
        validateUser(newUser);
        return userStorage.updateUserInformation(newUser);
    }

    public static boolean validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || user.getEmail().isBlank() || !user.getEmail()
                .contains("@")) {
            log.warn("Ошибка валидации пользователя: {} email не может существовать", user.getEmail());
            throw new ValidationException("Неверно указан email.");
        } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.warn("Ошибка валидации пользователя: {} login не может существовать", user.getLogin());
            throw new ValidationException("Неверно указан логин.");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Ошибка валидации пользователя: {} дата не валидна", user.getBirthday());
            throw new ValidationException("Неверно указана дата рождения.");
        }
        return true;
    }
}
