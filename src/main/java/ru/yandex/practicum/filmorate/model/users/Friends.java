package ru.yandex.practicum.filmorate.model.users;

import lombok.Data;

@Data
public class Friends {
    private long idUser;
    private long idFriendUser;
    private boolean isConfirmed;
}

