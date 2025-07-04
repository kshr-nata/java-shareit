package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingNewRequest;
import ru.practicum.shareit.item.dto.ItemUpdateRequest;

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
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") int userId, @Valid BookingNewRequest request) {
        return bookingService.create(userId, request);
    }

    @PatchMapping("{bookingId}")
    public void manageBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                              @PathVariable int id,
                              @RequestParam boolean approved) {
        bookingService.manageBooking(userId, id, approved);
    }

    @GetMapping("{bookingId}")
    public BookingDto findById(@RequestHeader("X-Sharer-User-Id") int userId,
                               @PathVariable int id) {
        return bookingService.findById(userId, id);
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
