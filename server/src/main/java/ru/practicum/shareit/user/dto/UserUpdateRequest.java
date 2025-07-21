package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    String name;
    @Email
    String email;

    public boolean hasEmail() {
        return ! (email == null || email.isBlank());
    }

    public boolean hasName() {
        return ! (name == null || name.isBlank());
    }

}
