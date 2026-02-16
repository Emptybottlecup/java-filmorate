package ru.yandex.practicum.filmorate.mappers;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.LikeDto;
import ru.yandex.practicum.filmorate.model.Like;

@UtilityClass
public class LikeMapper {
    public static LikeDto mapToLikeDto(Like like) {
        LikeDto likeDto = new LikeDto();

        likeDto.setIdFilm(likeDto.getIdFilm());
        likeDto.setIdUser(likeDto.getIdUser());

        return likeDto;
    }
}
