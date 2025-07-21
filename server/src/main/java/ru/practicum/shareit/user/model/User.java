package ru.practicum.shareit.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * TODO Sprint add-controllers.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @NotNull
    @NotEmpty
    String name;
    @NotNull
    @NotEmpty
    @Email
    String email;
}
