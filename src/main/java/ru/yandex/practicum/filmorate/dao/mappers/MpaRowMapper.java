package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.films.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MpaRowMapper implements RowMapper<Mpa> {
    @Override
    public Mpa mapRow(ResultSet rs, int num) throws SQLException {
        Mpa mpa = new Mpa();

        mpa.setId(rs.getLong("id"));
        mpa.setMpaName(rs.getString("mpa"));

        return mpa;
    }
}
