package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundId.FilmOrUser;
import ru.yandex.practicum.filmorate.exceptions.NotFoundId.NotFoundIdException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.dao.FriendsRepository;
import ru.yandex.practicum.filmorate.dto.user.FriendDto;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.dto.user.UserUpdateInformation;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.users.Friends;
import ru.yandex.practicum.filmorate.model.users.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendsRepository friendsRepository;

    public List<UserDto> getAllUsers() {
        return userStorage.getAllUsers()
                .stream()
                .map(UserMapper::mapToUserDto).toList();
    }

    public UserDto getUser(Long id) {
        return UserMapper.mapToUserDto(userStorage.getUser(id)
                .orElseThrow(() -> new NotFoundIdException(id, FilmOrUser.USER)));
    }

    public List<FriendDto> getUserFriends(Long id) {
        getUser(id);

        return friendsRepository.getFriends(id)
                .stream()
                .map(friends -> UserMapper.mapToFriendDto(getUser(friends.getIdFriendUser()),
                        friends.isConfirmed()))
                .toList();
    }

    public FriendDto addFriends(long idUser, long idUserFriend) {
        getUser(idUser);

        UserDto userFriend = getUser(idUserFriend);

        Friends friendsConnections = friendsRepository.getFriendsConnectionsTwoUsers(idUser, idUserFriend).orElseGet(
                () -> {
                    Friends friends = new Friends();
                    friends.setIdUser(idUser);
                    friends.setIdFriendUser(idUserFriend);
                    friends.setConfirmed(false);
                    friendsRepository.sendFriendRequest(friends).orElseThrow(() -> new InternalServerException(String
                                .format("Не получилось отправить заявку в друзья к пользователю с id = %d",
                                        idUserFriend)));
                    return friends;
                });

            return UserMapper.mapToFriendDto(userFriend, friendsConnections.isConfirmed());
    }

    public void deleteFriends(long idUser, long idUserFriend) {
        getUser(idUser);
        getUser(idUserFriend);
        friendsRepository.deleteFriendsConnection(idUser, idUserFriend);
    }

    public UserDto addUser(NewUserRequest newUserRequest) {
        validateUser(newUserRequest);

        if (userStorage.getUserByEmail(newUserRequest.getEmail()).isPresent()) {
           throw new ValidationException(String.format("Пользователь с email = %s уже существует", newUserRequest
                   .getEmail()));
        }
        if (userStorage.getUserByLogin(newUserRequest.getLogin()).isPresent()) {
            throw new ValidationException(String.format("Пользователь с login = %s уже существует", newUserRequest
                    .getLogin()));
        }

        Optional<User> user = userStorage.addNewUser(UserMapper.createUser(newUserRequest));

        if(user.isEmpty()) {
            throw new InternalServerException("Сервер не смог создать такого пользователя");
        }

        return UserMapper.mapToUserDto(user.get());
    }

    public UserDto updateUserInformation(UserUpdateInformation userUpdateInformation) {
        Long id = userUpdateInformation.getId();

        if(!userUpdateInformation.hasId()) {
            throw new ValidationException("Для обновления данных пользователя необходимо передать id пользователя");
        }

        if(userUpdateInformation.hasEmail()) {
            if(userStorage.getUserByEmail(userUpdateInformation.getEmail()).isPresent()) {
                throw new ValidationException(String.format("Пользователь с email = %s уже существует",
                        userUpdateInformation.getEmail()));
            }
        }


        if(userUpdateInformation.hasLogin()) {
            if(userStorage.getUserByLogin(userUpdateInformation.getLogin()).isPresent()) {
                throw new ValidationException(String.format("Пользователь с логином = %s уже существует",
                        userUpdateInformation.getLogin()));
            }
        }

        User user = UserMapper.updateUserInformation(userStorage.getUser(id).orElseThrow(() ->
                new NotFoundIdException(id, FilmOrUser.USER)), userUpdateInformation);

        return UserMapper.mapToUserDto(userStorage.updateUserInformation(user).orElseThrow(() ->
                new InternalServerException("Не получилось обновить данные пользователя")));
    }

    public void deleteUser(long id) {
        getUser(id);

        if (!userStorage.deleteUser(id)) {
            throw new InternalServerException("Не получилось удалить пользователя");
        }
    }

    public List<FriendDto> getCommonFriends(Long id, Long friendId) {
        getUser(id);
        getUser(friendId);

        return getUserFriends(id).stream()
                .filter(friendDto -> friendsRepository.getFriendsConnectionsTwoUsers(
                        friendId, friendDto.getId()).isPresent())
                .toList();
    }

    private void validateUser(NewUserRequest user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail()
                .contains("@")) {
            log.warn("Ошибка валидации пользователя: {} email не может существовать", user.getEmail());
            throw new ValidationException("Неверно указан email.");
        } else if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Ошибка валидации пользователя: {} login не может существовать", user.getLogin());
            throw new ValidationException("Неверно указан логин.");
        } else if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Ошибка валидации пользователя: {} дата не валидна", user.getBirthday());
            throw new ValidationException("Неверно указана дата рождения.");
        } if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
