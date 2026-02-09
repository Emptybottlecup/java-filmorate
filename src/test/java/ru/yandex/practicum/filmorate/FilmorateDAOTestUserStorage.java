package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DuplicateKeyException;
import ru.yandex.practicum.filmorate.dto.user.UserUpdateInformation;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.users.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate.dao")
class FilmorateDAOTestUserStorage {
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
    public void updateUserInformation() {
        User newUser = new User();
        newUser.setLogin("andrey1005");
        newUser.setBirthday(LocalDate.now());
        newUser.setEmail("dobr@3d3d33423dname.ru");
        newUser.setName("Andrey");
        Optional<User> addedNewUser = userStorage.addNewUser(newUser);

        UserUpdateInformation userUpdateInformation = new UserUpdateInformation();
        userUpdateInformation.setBirthday(LocalDate.of(1990,1,1));
        userUpdateInformation.setEmail("ooogreen@OOmail.com");
        userUpdateInformation.setLogin("SCUM");

        User user = userStorage.getUserById(addedNewUser.get().getId()).get();
        UserMapper.updateUserInformation(user, userUpdateInformation);
        userStorage.updateUserInformation(user);

        User userUpdated = userStorage.getUserById(addedNewUser.get().getId()).get();
        assertEquals(LocalDate.of(1990,1,1), userUpdated.getBirthday());
        assertEquals("ooogreen@OOmail.com", userUpdated.getEmail());
        assertEquals("SCUM", userUpdated.getLogin());

        user.setId(9999L);
        assertThat(userStorage.updateUserInformation(user)).isEmpty();
    }

    @Test
    public void testDeleteUser() {
        List<User> users = userStorage.getAllUsers();
        assertEquals(2, users.size());

        User newUser = new User();
        newUser.setLogin("andrey1005");
        newUser.setBirthday(LocalDate.now());
        newUser.setEmail("dobr@3d3d33423dname.ru");
        newUser.setName("Andrey");

        Optional<User> addedNewUser = userStorage.addNewUser(newUser);
        List<User> users2 = userStorage.getAllUsers();
        assertEquals(3, users2.size());
        assertTrue(userStorage.deleteUserById(addedNewUser.get().getId()));

        List<User> users3 = userStorage.getAllUsers();
        assertEquals(2, users3.size());

        assertThat(userStorage.deleteUserById(9999L)).isFalse();
    }

    @Test
    public void testAddNewUser() {
        User newUser = new User();
        newUser.setLogin("andrey100");
        newUser.setBirthday(LocalDate.now());
        newUser.setEmail("dobr@3d3d3dname.ru");
        newUser.setName("Andrey");

        Optional<User> addedNewUser = userStorage.addNewUser(newUser);
        List<User> users = userStorage.getAllUsers();
        assertThat(addedNewUser).isPresent();
        assertEquals(3, users.size());
        assertThat(userStorage.getUserById(addedNewUser.get().getId())).isPresent().hasValueSatisfying(user ->
                assertThat(user).hasFieldOrPropertyWithValue("id", addedNewUser.get().getId()));

        User newUser2 = new User();
        newUser2.setLogin("andrey2000");
        newUser2.setBirthday(LocalDate.now());
        newUser2.setEmail("dobreed2-00@.ru");
        newUser2.setName("Andrey2");

        Optional<User> addedNewUser2 = userStorage.addNewUser(newUser2);
        List<User> users2 = userStorage.getAllUsers();
        assertThat(addedNewUser2).isPresent();
        assertEquals(4, users2.size());
        assertThat(userStorage.getUserById(addedNewUser2.get().getId())).isPresent().hasValueSatisfying(user ->
                assertThat(user).hasFieldOrPropertyWithValue("id", addedNewUser2.get().getId()));
        assertThrows(DuplicateKeyException.class, () -> userStorage.addNewUser(newUser2));

        newUser2.setEmail("fwefefewfewfew@mail.com");
        assertThrows(DuplicateKeyException.class, () -> userStorage.addNewUser(newUser2));
    }

    @Test
    public void testGetUserById() {
        List<User> users = userStorage.getAllUsers();

        Optional<User> userFoundedById = userStorage.getUserById(users.getFirst().getId());
        assertThat(userFoundedById).isPresent().hasValueSatisfying(user -> assertThat(user)
                .hasFieldOrPropertyWithValue("id", users.getFirst().getId()));
        assertThat(userStorage.getUserById(99999L)).isEmpty();
    }

    @Test
    public void testGetAllUsers() {
        List<User> allUsers = userStorage.getAllUsers();
        assertEquals(2, allUsers.size());

        User newUser = new User();
        newUser.setLogin("andrey3");
        newUser.setBirthday(LocalDate.now());
        newUser.setEmail("dobr33@name.ru");
        newUser.setName("Andrey3");
        userStorage.addNewUser(newUser);

        List<User> allUsers2 = userStorage.getAllUsers();
        assertEquals(3, allUsers2.size());
    }
}
