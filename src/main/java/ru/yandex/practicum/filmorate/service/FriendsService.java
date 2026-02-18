package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.repository.FriendsRepository;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.model.users.Friends;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendsService {
    private final FriendsRepository friendsStorage;

    public List<Friends> getFriendsById(Long id) {
        return friendsStorage.getFriendsById(id);
    }

    public Friends addFriends(long idUser, long idUserFriend) {
        return friendsStorage.getFriendsConnectionsTwoUsers(idUser, idUserFriend)
                .orElseGet(() -> friendsStorage.addFriends(idUser, idUserFriend)
                        .orElseThrow(() -> new InternalServerException(String.format("Не получилось отправить заявку в " +
                                "друзья к пользователю с id = %d", idUserFriend))));
    }

    public void deleteFriends(long idUser, long idUserFriend) {
        friendsStorage.deleteFriends(idUser, idUserFriend);
    }

    public List<Friends> getCommonFriends(Long idUser, Long idUserFriend) {
        return friendsStorage.getFriendsById(idUser).stream()
                .filter(friend -> friendsStorage.getFriendsConnectionsTwoUsers(idUserFriend,
                        friend.getIdFriendUser()).isPresent()).toList();
    }
}
