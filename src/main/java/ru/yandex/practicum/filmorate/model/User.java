package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private int id;
    private String name;
    @Email
    private final String email;
    @NotBlank
    private final String login;
    @NotNull
    private final LocalDate birthday;
}