package ru.yandex.practicum.filmorate.dao.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.films.GenreAndFilm;

import java.util.List;
import java.util.Optional;

@Repository
public class LikesRepository extends BaseRepository<Like>{
    private final String QUERY_GET_ALL_LIKES_OF_FILM = "SELECT * FROM likes WHERE id_film = ?";
    private final String QUERY_INSERT_LIKE = "INSERT INTO likes (id_film, id_user) VALUES (?,?)";
    private final String QUERY_DELETE_LIKE = "DELETE FROM likes WHERE id_film = ? AND id_user = ?";

    public LikesRepository(JdbcTemplate jdbc, RowMapper<Like> mapper) {
        super(jdbc, mapper);
    }

    public boolean deleteLike(long idFilm, long idUser) {
        return update(QUERY_DELETE_LIKE, idFilm, idUser);
    }

    public List<Like> getLikesOfFilm(long idFilm) {
        return getMany(QUERY_GET_ALL_LIKES_OF_FILM, idFilm);
    }

    public Optional<Like> addLike(long idFilm, long idUser) {
        if(update(QUERY_INSERT_LIKE, idFilm, idUser)) {
            Like like = new Like();
            like.setIdFilm(idFilm);
            like.setIdUser(idUser);
            return Optional.of(like);
        }
        return Optional.empty();
    }
}
