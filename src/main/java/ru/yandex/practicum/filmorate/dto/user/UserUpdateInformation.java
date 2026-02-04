package ru.yandex.practicum.filmorate.dto.user;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateInformation {
    private Long id;
    private String name;
    private String email;
    private String login;
    private LocalDate birthday;

    public boolean hasId() {
        return id != null;
    }

    public boolean hasBirthDay() {
        return birthday != null;
    }

    public boolean hasName() {
        return name == null || !name.isBlank();
    }

    public boolean hasEmail() {
        return email != null && !email.isBlank();
    }

    public boolean hasLogin() {
        return login != null && !login.isBlank();
    }
}
