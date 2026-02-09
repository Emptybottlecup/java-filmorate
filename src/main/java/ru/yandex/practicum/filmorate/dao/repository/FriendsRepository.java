package ru.yandex.practicum.filmorate.dao.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mappers.FriendsRowMapper;
import ru.yandex.practicum.filmorate.model.users.Friends;

import java.util.List;
import java.util.Optional;

@Repository
public class FriendsRepository extends BaseRepository<Friends> {
    String QUERY_GET_USER_FRIENDS = "SELECT * FROM friends WHERE id_user = ?";
    String QUERY_ADD_NEW_FRIENDS_REQUEST = "INSERT INTO friends (id_user, id_friend_user, is_confirmed)" +
            "VALUES (?,?,?)";
    String QUERY_GET_FRIENDS_CONNECTIONS_TWO_USERS = "SELECT * FROM friends WHERE id_user = ? AND id_friend_user = ?";
    String QUERY_DELETE_FRIEND_CONNECTION = "DELETE FROM friends WHERE id_user = ? AND id_friend_user = ?";

    public FriendsRepository(JdbcTemplate jdbc, FriendsRowMapper mapper) {
        super(jdbc, mapper);
    }

    public List<Friends> getFriendsById(long id) {
        return getMany(QUERY_GET_USER_FRIENDS, id);
    }

    public Optional<Friends> addFriends(long idUser, long idUserFriend) {
        Friends friends = new Friends();
        friends.setIdUser(idUser);
        friends.setIdFriendUser(idUserFriend);
        friends.setConfirmed(false);
        if(update(QUERY_ADD_NEW_FRIENDS_REQUEST, friends.getIdUser(), friends.getIdFriendUser(), friends
                .isConfirmed())) {
            return Optional.of(friends);
        }
        return Optional.empty();
    }

    public boolean deleteFriends(long idUser, long idFriendUSer) {
        return update(QUERY_DELETE_FRIEND_CONNECTION, idUser, idFriendUSer);
    }

    public Optional<Friends> getFriendsConnectionsTwoUsers(long idUser, long idUserFriend) {
        return getOne(QUERY_GET_FRIENDS_CONNECTIONS_TWO_USERS, idUser, idUserFriend);
    }
}
