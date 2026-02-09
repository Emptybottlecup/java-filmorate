package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateInformation {
    @NotNull @Positive
    private Long id;
    private String name;
    @Email
    private String email;
    @Pattern(regexp = "\\S+")
    private String login;
    @PastOrPresent
    private LocalDate birthday;

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
