package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.users.Friends;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FriendsRowMapper implements RowMapper<Friends> {
    @Override
    public Friends mapRow(ResultSet rs, int num) throws SQLException {
        Friends friends = new Friends();

        friends.setIdFriendUser(rs.getLong("id_friend_user"));
        friends.setIdUser(rs.getLong("id_user"));
        friends.setConfirmed(rs.getBoolean("is_confirmed"));

        return friends;
    }
}
