package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Like;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LikeRowMapper implements RowMapper<Like> {
    @Override
    public Like mapRow(ResultSet rs, int num) throws SQLException {
        Like like = new Like();

        like.setIdFilm(rs.getLong("id_film"));
        like.setIdUser(rs.getLong("id_user"));

        return like;
    }
}
