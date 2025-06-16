package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserUpdateRequest;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public Collection<UserDto> findAll() {
        return userStorage.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto create(User request) {
        if (userStorage.findUserByEmail(request.getEmail()).isPresent()) {
            throw new ConflictException("Пользователь с email " + request.getEmail() + " уже существует!");
        }
        User user = userStorage.create(request);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto update(int id, UserUpdateRequest request) {
        User user = userStorage.findUserById(id).orElseThrow(()
                -> new NotFoundException(String.format("Пользователь с id %d не найден",
                id)));
        if (userStorage.findUserByEmail(request.getEmail()).isPresent()
                && userStorage.findUserByEmail(request.getEmail()).get().getId() != id) {
            throw new ConflictException("Пользователь с email " + request.getEmail() + " уже существует!");
        }
        user = UserMapper.updateUserFields(user, request);
        user = userStorage.update(id, user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto findUserById(int id) {
        log.debug("Вызван метод findUserById id = {}", id);
        return userStorage.findUserById(id)
                .map(UserMapper::toUserDto)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", id)));
    }

    @Override
    public void deleteUserById(int id) {
        userStorage.deleteUserById(id);
    }
}
