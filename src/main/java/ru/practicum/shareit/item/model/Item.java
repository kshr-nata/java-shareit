package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.HashSet;
import java.util.Set;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    private String description;
    @NotNull
    @Column(name="is_available")
    private Boolean available;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;
//    @JoinColumn(name = "user_id")
//    private ItemRequest request;
    @CollectionTable(name="comments", joinColumns=@JoinColumn(name="item_id"))
    @Column(name="text")
    private Set<String> comments = new HashSet<>();
}
