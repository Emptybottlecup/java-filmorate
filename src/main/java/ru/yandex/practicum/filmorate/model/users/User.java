package ru.yandex.practicum.filmorate.model.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private Long id;
    private String name;
    private String email;
    private String login;
    private LocalDate birthday;
}