package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.repository.LikesRepository;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikesRepository likesRepository;

    public Like addLike(long idFilm, long idUser) {
        return likesRepository.addLike(idFilm, idUser).orElseThrow(() -> new InternalServerException("Не получилось добавить лайк"));
    }

    public void deleteLike(long idFilm, long idUser) {
        likesRepository.deleteLike(idFilm, idUser);
    }

    public List<Like> getLikesOfFilm(long idUser) {
        return likesRepository.getLikesOfFilm(idUser);
    }
}
