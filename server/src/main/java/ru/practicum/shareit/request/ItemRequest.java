package ru.practicum.shareit.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
@ToString
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @NotNull
    @NotEmpty
    String description;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "requestor_id")
    User requestor;
    @NotNull
    @Column(updatable = false)
    LocalDateTime created;
}
