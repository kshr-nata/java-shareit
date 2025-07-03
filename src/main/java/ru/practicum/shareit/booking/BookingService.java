package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingNewRequest;

import java.util.List;

public interface BookingService {

    BookingDto create(int userId, BookingNewRequest request);

    void manageBooking(int userId, int bookingId, boolean approved);

    BookingDto findById(int userId, int bookingId);

    List<BookingDto> findByBookerId(int userId, BookingState state);

    List<BookingDto> findByOwnerId(int userId, BookingState state);
}
