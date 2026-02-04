package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.users.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> getAllUsers();

    Optional<User> addNewUser(User user);

    Optional<User> updateUserInformation(User user);

    Optional<User> getUser(Long id);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByLogin(String login);

    boolean deleteUser(long id);
}
