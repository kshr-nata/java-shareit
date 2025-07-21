package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateRequest;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {

    Collection<UserDto> findAll();

    UserDto create(User user);

    UserDto update(int id, UserUpdateRequest user);

    UserDto findUserById(int id);

    void deleteUserById(int id);
}
