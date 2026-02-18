package ru.yandex.practicum.filmorate.dao.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.films.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaRepository extends BaseRepository<Mpa> {
    private static final String QUERY_GET_ALL_MPAS = "SELECT * FROM mpas ORDER BY id ASC";
    private static final String QUERY_GET_MPA_BY_ID = "SELECT * FROM mpas WHERE id = ?";

    public MpaRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public List<Mpa> getAllMpa() {
        return getMany(QUERY_GET_ALL_MPAS);
    }

    public Optional<Mpa> getMpaById(long id) {
        return getOne(QUERY_GET_MPA_BY_ID, id);
    }

}
