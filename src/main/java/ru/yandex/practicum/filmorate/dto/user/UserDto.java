package ru.yandex.practicum.filmorate.dto.user;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String login;
    private LocalDate birthday;
    private String name;
}
