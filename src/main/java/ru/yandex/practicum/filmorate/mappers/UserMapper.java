package ru.yandex.practicum.filmorate.mappers;

import ru.yandex.practicum.filmorate.dto.user.FriendDto;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.dto.user.UserUpdateInformation;
import ru.yandex.practicum.filmorate.model.users.User;

public class UserMapper {
     public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setName(user.getName());
        userDto.setBirthday(user.getBirthday());
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setLogin(user.getLogin());

        return userDto;
    }

     public static User updateUserInformation(User user, UserUpdateInformation userUpdateInformation) {
        if (userUpdateInformation.hasEmail()) {
            user.setEmail(userUpdateInformation.getEmail());
        }
        if (userUpdateInformation.hasLogin()) {
            user.setLogin(userUpdateInformation.getLogin());
        }
        if (userUpdateInformation.hasBirthDay()) {
            user.setBirthday(userUpdateInformation.getBirthday());
        }
        if (userUpdateInformation.hasName()) {
            user.setName(userUpdateInformation.getName());
        }
        return user;
    }

     public static User createUser(NewUserRequest newUserRequest) {
        User user = new User();

        user.setBirthday(newUserRequest.getBirthday());
        user.setLogin(newUserRequest.getLogin());
        user.setName(newUserRequest.getName());
        user.setEmail(newUserRequest.getEmail());

        return user;
    }

     public static FriendDto mapToFriendDto(UserDto user, boolean isConfirmed) {
        FriendDto friendDto = new FriendDto();

        friendDto.setIsConfirmed(isConfirmed);
        friendDto.setEmail(user.getEmail());
        friendDto.setId(user.getId());
        friendDto.setLogin(user.getLogin());

        return friendDto;
    }
}
