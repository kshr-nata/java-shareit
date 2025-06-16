package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    Collection<User> findAll();
    User create(User user);
    User update(int id, User user);
    Optional<User> findUserById(int id);
    Optional<User> findUserByEmail(String email);
    void deleteUserById(int id);
}
