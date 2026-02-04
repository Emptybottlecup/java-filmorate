package ru.yandex.practicum.filmorate.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotFriendsException extends RuntimeException {
    private final long idUser;
    private final long idUserFriend;
}
