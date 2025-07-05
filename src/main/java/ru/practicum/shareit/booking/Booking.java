package ru.practicum.shareit.booking;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@ToString
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @NotNull
    @Column(name="start_date")
    LocalDateTime start;
    @NotNull
    @Column(name="end_date")
    LocalDateTime end;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "item_id")
    Item item;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "booker_id")
    User booker;
    @NotNull
    @Enumerated(EnumType.STRING)
    BookingStatus status;
}
