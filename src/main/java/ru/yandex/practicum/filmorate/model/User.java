package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Set<Long> friends = new HashSet<>();
    private Long id;
    private String name;
    @Email
    private final String email;
    @NotBlank
    private final String login;
    @NotNull
    private final LocalDate birthday;
}