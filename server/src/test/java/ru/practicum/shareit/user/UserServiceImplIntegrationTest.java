package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateRequest;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceImplIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void update_shouldUpdateUserFields() {
        // Создаем исходного пользователя
        User originalUser = new User();
        originalUser.setName("Original Name");
        originalUser.setEmail("original@example.com");
        originalUser = userRepository.save(originalUser);

        // Подготавливаем запрос на обновление
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setName("Updated Name");
        updateRequest.setEmail("updated@example.com");

        // Вызываем тестируемый метод
        UserDto updatedUser = userService.update(originalUser.getId(), updateRequest);

        // Проверяем результаты
        assertNotNull(updatedUser, "Обновленный пользователь не должен быть null");
        assertEquals(originalUser.getId(), updatedUser.getId(), "ID должен остаться прежним");
        assertEquals("Updated Name", updatedUser.getName(), "Имя должно обновиться");
        assertEquals("updated@example.com", updatedUser.getEmail(), "Email должен обновиться");
    }

    @Test
    void update_shouldUpdateOnlyNameWhenEmailNotProvided() {
        // Создаем исходного пользователя
        User originalUser = new User();
        originalUser.setName("Original Name");
        originalUser.setEmail("original@example.com");
        originalUser = userRepository.save(originalUser);

        // Подготавливаем запрос на обновление (только имя)
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setName("Updated Name");

        // Вызываем тестируемый метод
        UserDto updatedUser = userService.update(originalUser.getId(), updateRequest);

        // Проверяем результаты
        assertEquals("Updated Name", updatedUser.getName(), "Имя должно обновиться");
        assertEquals("original@example.com", updatedUser.getEmail(), "Email должен остаться прежним");
    }

    @Test
    void update_shouldUpdateOnlyEmailWhenNameNotProvided() {
        // Создаем исходного пользователя
        User originalUser = new User();
        originalUser.setName("Original Name");
        originalUser.setEmail("original@example.com");
        originalUser = userRepository.save(originalUser);

        // Подготавливаем запрос на обновление (только email)
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setEmail("updated@example.com");

        // Вызываем тестируемый метод
        UserDto updatedUser = userService.update(originalUser.getId(), updateRequest);

        // Проверяем результаты
        assertEquals("Original Name", updatedUser.getName(), "Имя должно остаться прежним");
        assertEquals("updated@example.com", updatedUser.getEmail(), "Email должен обновиться");
    }

    @Test
    void update_whenEmailAlreadyExists_shouldThrowConflictException() {
        // Создаем первого пользователя
        User existingUser = new User();
        existingUser.setName("Existing User");
        existingUser.setEmail("existing@example.com");
        userRepository.save(existingUser);

        // Создаем второго пользователя для обновления
        User userToUpdate = new User();
        userToUpdate.setName("User To Update");
        userToUpdate.setEmail("unique@example.com");
        userToUpdate = userRepository.save(userToUpdate);

        // Подготавливаем запрос на обновление с существующим email
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setEmail("existing@example.com");

        // Проверяем, что выбрасывается исключение
        User finalUserToUpdate = userToUpdate;
        assertThrows(ConflictException.class, () -> {
            userService.update(finalUserToUpdate.getId(), updateRequest);
        }, "Должно быть выброшено ConflictException при попытке использовать существующий email");
    }

    @Test
    void update_whenUserNotFound_shouldThrowNotFoundException() {
        // Подготавливаем запрос на обновление
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setName("Updated Name");
        updateRequest.setEmail("updated@example.com");

        // Проверяем, что выбрасывается исключение
        assertThrows(NotFoundException.class, () -> {
            userService.update(999, updateRequest);
        }, "Должно быть выброшено NotFoundException при попытке обновления несуществующего пользователя");
    }

    @Test
    void update_whenNoFieldsToUpdate_shouldReturnOriginalUser() {
        // Создаем исходного пользователя
        User originalUser = new User();
        originalUser.setName("Original Name");
        originalUser.setEmail("original@example.com");
        originalUser = userRepository.save(originalUser);

        // Подготавливаем пустой запрос на обновление
        UserUpdateRequest updateRequest = new UserUpdateRequest();

        // Вызываем тестируемый метод
        UserDto updatedUser = userService.update(originalUser.getId(), updateRequest);

        // Проверяем, что пользователь не изменился
        assertEquals("Original Name", updatedUser.getName(), "Имя должно остаться прежним");
        assertEquals("original@example.com", updatedUser.getEmail(), "Email должен остаться прежним");
    }
}