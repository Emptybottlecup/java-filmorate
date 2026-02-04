package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.users.Friends;

import java.util.List;
import java.util.Optional;

public interface FriendsStorage {

    List<Friends> getFriends(long id);

    Optional<Friends> sendFriendRequest(Friends friends);

    boolean deleteFriendsConnection(long idUser, long idFriendUSer);

    Optional<Friends> getFriendsConnectionsTwoUsers(long idUser, long idUserFriend);
}
