package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
public class BookingInfo {
    LocalDate start;
    LocalDate end;
}
