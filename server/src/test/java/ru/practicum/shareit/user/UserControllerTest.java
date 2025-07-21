package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateRequest;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private final UserDto userDto = new UserDto(1, "User", "user@email.com");
    private final User user = new User(1, "User", "user@email.com");

    @Test
    void findAll_shouldReturnEmptyList() throws Exception {
        Mockito.when(userService.findAll())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        Mockito.verify(userService, Mockito.times(1)).findAll();
    }

    @Test
    void findAll_shouldReturnListOfUsers() throws Exception {
        Mockito.when(userService.findAll())
                .thenReturn(Collections.singletonList(userDto));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("User"))
                .andExpect(jsonPath("$[0].email").value("user@email.com"));

        Mockito.verify(userService, Mockito.times(1)).findAll();
    }

    @Test
    void create_shouldReturnCreatedUser() throws Exception {
        Mockito.when(userService.create(any(User.class)))
                .thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("User"))
                .andExpect(jsonPath("$.email").value("user@email.com"));

        Mockito.verify(userService, Mockito.times(1)).create(any(User.class));
    }

    @Test
    void create_shouldReturnBadRequestWhenNameIsEmpty() throws Exception {
        User invalidUser = new User(1, "", "user@email.com");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());

        Mockito.verify(userService, Mockito.never()).create(any(User.class));
    }

    @Test
    void create_shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
        User invalidUser = new User(1, "User", "invalid-email");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());

        Mockito.verify(userService, Mockito.never()).create(any(User.class));
    }

    @Test
    void update_shouldReturnUpdatedUser() throws Exception {
        UserUpdateRequest updateRequest = new UserUpdateRequest("Updated", "updated@email.com");
        UserDto updatedUser = new UserDto(1, "Updated", "updated@email.com");

        Mockito.when(userService.update(anyInt(), any(UserUpdateRequest.class)))
                .thenReturn(updatedUser);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.email").value("updated@email.com"));

        Mockito.verify(userService, Mockito.times(1)).update(anyInt(), any(UserUpdateRequest.class));
    }

    @Test
    void update_shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
        UserUpdateRequest invalidUpdate = new UserUpdateRequest(null, "invalid-email");

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdate)))
                .andExpect(status().isBadRequest());

        Mockito.verify(userService, Mockito.never()).update(anyInt(), any(UserUpdateRequest.class));
    }

    @Test
    void findUserById_shouldReturnUser() throws Exception {
        Mockito.when(userService.findUserById(anyInt()))
                .thenReturn(userDto);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("User"))
                .andExpect(jsonPath("$.email").value("user@email.com"));

        Mockito.verify(userService, Mockito.times(1)).findUserById(1);
    }

    @Test
    void findUserById_shouldReturnNotFoundForNonExistingUser() throws Exception {
        Mockito.when(userService.findUserById(anyInt()))
                .thenThrow(new NotFoundException("User not found"));

        mockMvc.perform(get("/users/999"))
                .andExpect(status().isNotFound());

        Mockito.verify(userService, Mockito.times(1)).findUserById(999);
    }

    @Test
    void deleteUserById_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1)).deleteUserById(1);
    }
}
