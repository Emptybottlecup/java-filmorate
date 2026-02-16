package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundId.WhichObjectNotFound;
import ru.yandex.practicum.filmorate.exceptions.NotFoundId.NotFoundIdException;
import ru.yandex.practicum.filmorate.dto.user.FriendDto;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.dto.user.UserUpdateInformation;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.users.Friends;
import ru.yandex.practicum.filmorate.model.users.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendsService friendsService;

    public List<UserDto> getAllUsers() {
        return userStorage.getAllUsers().stream().map(UserMapper::mapToUserDto).toList();
    }

    public UserDto getUser(Long id) {
        return UserMapper.mapToUserDto(userStorage.getUserById(id).orElseThrow(() -> new NotFoundIdException(id, WhichObjectNotFound.USER)));
    }

    public List<FriendDto> getFriendsById(Long id) {
        getUser(id);

        return friendsService.getFriendsById(id).stream()
                .map(friends -> UserMapper.mapToFriendDto(getUser(friends.getIdFriendUser()), friends.isConfirmed()))
                .toList();
    }

    public FriendDto addFriends(long idUser, long idUserFriend) {
        getUser(idUser);

        UserDto userFriend = getUser(idUserFriend);

        Friends friends = friendsService.addFriends(idUser, idUserFriend);

        return UserMapper.mapToFriendDto(userFriend, friends.isConfirmed());
    }

    public void deleteFriends(long idUser, long idUserFriend) {
        getUser(idUser);
        getUser(idUserFriend);

        friendsService.deleteFriends(idUser, idUserFriend);
    }

    public UserDto addUser(NewUserRequest newUserRequest) {
        Optional<User> user = userStorage.addNewUser(UserMapper.createUser(newUserRequest));

        if (user.isEmpty()) {
            throw new InternalServerException("Сервер не смог создать такого пользователя");
        }

        return UserMapper.mapToUserDto(user.get());
    }

    public UserDto updateUserInformation(UserUpdateInformation userUpdateInformation) {
        Long id = userUpdateInformation.getId();

        User user = UserMapper.updateUserInformation(userStorage.getUserById(id).orElseThrow(() -> new NotFoundIdException(id, WhichObjectNotFound.USER)), userUpdateInformation);

        return UserMapper.mapToUserDto(userStorage.updateUserInformation(user).orElseThrow(() -> new InternalServerException("Не получилось обновить данные пользователя")));
    }

    public void deleteUser(long id) {
        getUser(id);

        if (!userStorage.deleteUserById(id)) {
            throw new InternalServerException("Не получилось удалить пользователя");
        }
    }

    public List<FriendDto> getCommonFriends(Long idUser, Long idUserFriend) {
        getUser(idUser);
        getUser(idUserFriend);

        return friendsService.getCommonFriends(idUser, idUserFriend).stream().map(friend -> UserMapper.mapToFriendDto(getUser(friend.getIdFriendUser()), friend.isConfirmed())).toList();
    }
}
