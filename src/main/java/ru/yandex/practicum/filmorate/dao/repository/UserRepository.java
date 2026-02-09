package ru.yandex.practicum.filmorate.dao.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.users.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository extends BaseRepository<User> implements UserStorage {
    private static final String QUERY_GET_ALL_USERS = "SELECT * FROM users";
    private static final String QUERY_GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String QUERY_INSERT_NEW_USER = "INSERT INTO users (name, email, login, birthday)" + " VALUES(?, ?, ?, ?)";
    private static final String QUERY_UPDATE_USER_INFORMATION = "UPDATE users SET name = ?, email = ?, login = ?," + "birthday = ? WHERE id = ?";
    private static final String QUERY_GET_USER_BY_LOGIN = "SELECT * FROM users WHERE login = ?";
    private static final String QUERY_GET_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
    private static final String QUERY_DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ?";

    public UserRepository(JdbcTemplate jdbc, UserRowMapper mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<User> getAllUsers() {
        return getMany(QUERY_GET_ALL_USERS);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return getOne(QUERY_GET_USER_BY_ID, id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return getOne(QUERY_GET_USER_BY_EMAIL, email);
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        return getOne(QUERY_GET_USER_BY_LOGIN, login);
    }

    @Override
    public Optional<User> addNewUser(User user) {
        Optional<Long> id = insert(QUERY_INSERT_NEW_USER, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday());
        if (id.isEmpty()) {
            return Optional.empty();
        }
        user.setId(id.get());
        return Optional.of(user);
    }

    @Override
    public boolean deleteUserById(long id) {
        return update(QUERY_DELETE_USER_BY_ID, id);
    }

    @Override
    public Optional<User> updateUserInformation(User user) {
        boolean isUpdated = update(QUERY_UPDATE_USER_INFORMATION, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday(), user.getId());

        if (!isUpdated) {
            return Optional.empty();
        }
        return Optional.of(user);
    }
}
