package ru.yandex.practicum.filmorate.dto.user;

import lombok.Data;

@Data
public class FriendDto {
    private Long id;
    private String email;
    private String login;
    private Boolean isConfirmed;
}
