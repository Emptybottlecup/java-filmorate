package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

@Data
public class LikeDto {
    private long idFilm;
    private long idUser;
}
