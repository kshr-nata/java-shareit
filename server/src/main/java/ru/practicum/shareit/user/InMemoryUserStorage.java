package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private final Map<String, User> usersByEmail = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
        user.setId(getNextId());
        // сохраняем нового пользователя в памяти приложения
        users.put(user.getId(), user);
        usersByEmail.put(user.getEmail(), user);
        log.info("Создан пользователь {}", user);
        return user;
    }

    @Override
    public User update(int id, User newUser) {
        if (users.containsKey(id)) {
            User oldUser = users.get(id);
            if (newUser.getEmail() != null) {
                usersByEmail.remove(oldUser.getEmail());
                usersByEmail.put(newUser.getEmail(), newUser);
                oldUser.setEmail(newUser.getEmail());
            }
            if (newUser.getName() != null) {
                oldUser.setName(newUser.getName());
            }
            log.info("Данные пользователя {} обновлены", oldUser);
            return oldUser;
        }
        log.warn("Пользователь с id = {} не найден", id);
        throw new NotFoundException("Пользователь с id = " + id + " не найден");
    }

    @Override
    public Optional<User> findUserById(int id) {
        User returnedUser = null;
        User user = users.get(id);
        if (user != null) {
            returnedUser = new User();
            returnedUser.setEmail(user.getEmail());
            returnedUser.setId(user.getId());
            returnedUser.setName(user.getName());
        }
        return Optional.ofNullable(returnedUser);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return Optional.ofNullable(usersByEmail.get(email));
    }

    @Override
    public void deleteUserById(int id) {
        User user = findUserById(id).orElseThrow(() ->
        new NotFoundException("Пользователь с id = " + id + " не найден"));
        users.remove(id);
        usersByEmail.remove(user.getEmail());
    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    private int getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
