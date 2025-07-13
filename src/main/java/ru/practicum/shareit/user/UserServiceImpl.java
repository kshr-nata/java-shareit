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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Collection<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto create(User request) {
        if (!userRepository.findByEmail(request.getEmail()).isEmpty()) {
            throw new ConflictException("Пользователь с email " + request.getEmail() + " уже существует!");
        }
        return UserMapper.toUserDto(userRepository.save(request));
    }

    @Override
    public UserDto update(int id, UserUpdateRequest request) {
        User user = userRepository.findById(id).orElseThrow(()
                -> new NotFoundException(String.format("Пользователь с id %d не найден",
                id)));
        if (!userRepository.findByEmailIsAndIdNot(request.getEmail(), id).isEmpty()) {
            throw new ConflictException("Пользователь с email " + request.getEmail() + " уже существует!");
        }
        return UserMapper.toUserDto(userRepository.save(UserMapper.updateUserFields(user, request)));
    }

    @Override
    public UserDto findUserById(int id) {
        log.debug("Вызван метод findUserById id = {}", id);
        return userRepository.findById(id)
                .map(UserMapper::toUserDto)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", id)));
    }

    @Override
    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }
}
