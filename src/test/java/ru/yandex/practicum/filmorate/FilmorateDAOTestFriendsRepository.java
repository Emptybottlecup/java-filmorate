package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import ru.yandex.practicum.filmorate.dao.repository.FriendsRepository;
import ru.yandex.practicum.filmorate.model.users.Friends;
import ru.yandex.practicum.filmorate.model.users.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate.dao")
public class FilmorateDAOTestFriendsRepository {
    private final FriendsRepository friendsRepository;
    private final UserStorage userStorage;

    @BeforeEach
    public void addUsers() {
        User newUser = new User();
        newUser.setLogin("andrey");
        newUser.setBirthday(LocalDate.now());
        newUser.setEmail("dobr@name.ru");
        newUser.setName("Andrey");
        userStorage.addNewUser(newUser);

        User newUser2 = new User();
        newUser2.setLogin("andrey2");
        newUser2.setBirthday(LocalDate.now());
        newUser2.setEmail("dobreed@.ru");
        newUser2.setName("Andrey2");
        userStorage.addNewUser(newUser2);
    }

    @Test
    public void addFriends() {
        List<User> users = userStorage.getAllUsers();
        assertThat(friendsRepository.addFriends(users.get(0).getId(), users.get(1).getId())).isPresent();

        assertThrows(DuplicateKeyException.class, () -> friendsRepository.addFriends(users.get(0).getId(), users.get(1)
                .getId()));

        assertThrows(DataIntegrityViolationException.class, () -> friendsRepository.addFriends(999999L, 1000000L));
    }

    @Test
    public void getFriendsById() {
        List<User> users = userStorage.getAllUsers();
        friendsRepository.addFriends(users.get(0).getId(), users.get(1).getId());

        List<Friends> listOfFriendFirstUser = friendsRepository.getFriendsById(users.get(0).getId());
        assertEquals(1, listOfFriendFirstUser.size());


        List<Friends> listOfFriendSecondUser = friendsRepository.getFriendsById(users.get(1).getId());
        assertEquals(0, listOfFriendSecondUser.size());
    }

    @Test
    public void deleteFriend() {
        List<User> users = userStorage.getAllUsers();
        friendsRepository.addFriends(users.get(0).getId(), users.get(1).getId());

        assertThat(friendsRepository.deleteFriends(users.get(0).getId(),users.get(1).getId())).isTrue();
        assertEquals(0, friendsRepository.getFriendsById(users.get(0).getId()).size());
        assertThat(friendsRepository.deleteFriends(users.get(1).getId(),users.get(0).getId())).isFalse();
    }

    @Test
    public void getFriendsConnectionsTwoUsers() {
        List<User> users = userStorage.getAllUsers();
        friendsRepository.addFriends(users.get(0).getId(), users.get(1).getId());

        assertThat(friendsRepository.getFriendsConnectionsTwoUsers(users.get(0).getId(),users.get(1).getId())).isPresent();
        assertThat(friendsRepository.getFriendsConnectionsTwoUsers(users.get(1).getId(),users.get(0).getId())).isEmpty();
    }
}
