package ru.yandex.practicum.filmorate.dao.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BaseRepository<T> {
    private final JdbcTemplate jdbc;
    private final RowMapper<T> mapper;

    protected List<T> getMany(String query, Object... params) {
        return jdbc.query(query, mapper, params);
    }

    protected Optional<T> getOne(String query, Object... params) {
        try {
            return Optional.ofNullable(jdbc.queryForObject(query, mapper, params));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    protected boolean update(String query, Object... params) {
        return jdbc.update(query, params) > 0;
    }

    protected Optional<Long> insert(String query, Object... params) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; ++idx) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;
        }, keyHolder);
        return Optional.ofNullable(keyHolder.getKeyAs(Long.class));
    }
}
