package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundId.FilmOrUser;
import ru.yandex.practicum.filmorate.Exceptions.NotFoundId.NotFoundIdException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private Long lastId = 1L;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(Long id) {
        if (!users.containsKey(id)) {
            log.warn("ID {} пользователя не был найден", id);
            throw new NotFoundIdException(id, FilmOrUser.USER);
        }
        User user = users.get(id);
        log.debug("Данные пользователя {} были успешно получены", user.getLogin());
        return user;
    }

    @Override
    public User addNewUser(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(lastId);
        users.put(lastId, user);
        ++lastId;
        log.debug("Был добавлен новый пользователь {}", user.getLogin());
        return user;
    }

    @Override
    public User updateUserInformation(User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("ID {} пользователя не был найден", user.getId());
            throw new NotFoundIdException(user.getId(), FilmOrUser.USER);
        }
        users.put(user.getId(), user);
        log.debug("Данные пользователя {} были обновлены", user.getLogin());
        return user;
    }
}
