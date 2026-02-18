package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.user.FriendDto;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.dto.user.UserUpdateInformation;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping("/{id}/friends")
    public List<FriendDto> getUserFriends(@PathVariable Long id) {
        return userService.getFriendsById(id);
    }

    @GetMapping("{id}/friends/common/{friendId}")
    public List<FriendDto> getCommonFriends(@PathVariable Long id, @PathVariable Long friendId) {
        return userService.getCommonFriends(id, friendId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public FriendDto addFriends(@PathVariable Long id, @PathVariable Long friendId) {
        return userService.addFriends(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriends(id, friendId);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
    }

    @PostMapping
    public UserDto addUser(@RequestBody @Valid NewUserRequest user) {
        return userService.addUser(user);
    }


    @PutMapping
    public UserDto updateUserInformation(@RequestBody @Valid UserUpdateInformation updateUserInformation) {
        return userService.updateUserInformation(updateUserInformation);
    }
}