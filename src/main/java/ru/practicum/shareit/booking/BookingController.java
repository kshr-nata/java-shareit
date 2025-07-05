package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingNewRequest;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") int userId, @Valid @RequestBody BookingNewRequest request) {
        return bookingService.create(userId, request);
    }

    @PatchMapping("{bookingId}")
    public BookingDto manageBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                              @PathVariable int bookingId,
                              @RequestParam boolean approved) {
        return bookingService.manageBooking(userId, bookingId, approved);
    }

    @GetMapping("{bookingId}")
    public BookingDto findById(@RequestHeader("X-Sharer-User-Id") int userId,
                               @PathVariable int bookingId) {
        return bookingService.findById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> findByBookerId(@RequestHeader("X-Sharer-User-Id") int userId,
                                         @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.findByBookerId(userId, state);
    }

    @GetMapping("owner")
    public List<BookingDto> findByOwnerId(@RequestHeader("X-Sharer-User-Id") int userId,
                                           @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.findByOwnerId(userId, state);
    }
}
