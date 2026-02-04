package ru.yandex.practicum.filmorate.model.users;

import lombok.Data;

@Data
public class Friends {
    long idUser;
    long idFriendUser;
    boolean isConfirmed;
}

