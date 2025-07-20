package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingNewRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @Test
    void create_shouldCreateNewBooking() {
        // Arrange
        int userId = 1;
        BookingNewRequest request = new BookingNewRequest();
        request.setItemId(1);

        BookingDto expectedBooking = new BookingDto();
        expectedBooking.setId(1);

        when(bookingService.create(userId, request)).thenReturn(expectedBooking);

        // Act
        BookingDto result = bookingController.create(userId, request);

        // Assert
        assertEquals(expectedBooking, result);
        verify(bookingService).create(userId, request);
    }

    @Test
    void manageBooking_shouldApproveBooking() {
        // Arrange
        int userId = 1;
        int bookingId = 1;
        boolean approved = true;

        BookingDto expectedBooking = new BookingDto();
        expectedBooking.setId(bookingId);
        expectedBooking.setStatus(BookingStatus.APPROVED);

        when(bookingService.manageBooking(userId, bookingId, approved)).thenReturn(expectedBooking);

        // Act
        BookingDto result = bookingController.manageBooking(userId, bookingId, approved);

        // Assert
        assertEquals(expectedBooking, result);
        assertEquals(BookingStatus.APPROVED, result.getStatus());
        verify(bookingService).manageBooking(userId, bookingId, approved);
    }

    @Test
    void manageBooking_shouldRejectBooking() {
        // Arrange
        int userId = 1;
        int bookingId = 1;
        boolean approved = false;

        BookingDto expectedBooking = new BookingDto();
        expectedBooking.setId(bookingId);
        expectedBooking.setStatus(BookingStatus.REJECTED);

        when(bookingService.manageBooking(userId, bookingId, approved)).thenReturn(expectedBooking);

        // Act
        BookingDto result = bookingController.manageBooking(userId, bookingId, approved);

        // Assert
        assertEquals(expectedBooking, result);
        assertEquals(BookingStatus.REJECTED, result.getStatus());
        verify(bookingService).manageBooking(userId, bookingId, approved);
    }

    @Test
    void findById_shouldReturnBooking() {
        // Arrange
        int userId = 1;
        int bookingId = 1;

        BookingDto expectedBooking = new BookingDto();
        expectedBooking.setId(bookingId);

        when(bookingService.findById(userId, bookingId)).thenReturn(expectedBooking);

        // Act
        BookingDto result = bookingController.findById(userId, bookingId);

        // Assert
        assertEquals(expectedBooking, result);
        verify(bookingService).findById(userId, bookingId);
    }

    @Test
    void findByBookerId_shouldReturnListOfBookings() {
        // Arrange
        int userId = 1;
        BookingState state = BookingState.ALL;

        BookingDto booking1 = new BookingDto();
        booking1.setId(1);
        BookingDto booking2 = new BookingDto();
        booking2.setId(2);
        List<BookingDto> expectedBookings = List.of(booking1, booking2);

        when(bookingService.findByBookerId(userId, state)).thenReturn(expectedBookings);

        // Act
        List<BookingDto> result = bookingController.findByBookerId(userId, state);

        // Assert
        assertEquals(2, result.size());
        assertEquals(expectedBookings, result);
        verify(bookingService).findByBookerId(userId, state);
    }

    @Test
    void findByBookerId_withDefaultState_shouldUseAllState() {
        // Arrange
        int userId = 1;

        BookingDto booking = new BookingDto();
        booking.setId(1);
        List<BookingDto> expectedBookings = List.of(booking);

        when(bookingService.findByBookerId(userId, BookingState.ALL)).thenReturn(expectedBookings);

        // Act
        List<BookingDto> result = bookingController.findByBookerId(userId, BookingState.ALL);

        // Assert
        assertEquals(expectedBookings, result);
        verify(bookingService).findByBookerId(userId, BookingState.ALL);
    }

    @Test
    void findByOwnerId_shouldReturnListOfBookings() {
        // Arrange
        int userId = 1;
        BookingState state = BookingState.CURRENT;

        BookingDto booking1 = new BookingDto();
        booking1.setId(1);
        BookingDto booking2 = new BookingDto();
        booking2.setId(2);
        List<BookingDto> expectedBookings = List.of(booking1, booking2);

        when(bookingService.findByOwnerId(userId, state)).thenReturn(expectedBookings);

        // Act
        List<BookingDto> result = bookingController.findByOwnerId(userId, state);

        // Assert
        assertEquals(2, result.size());
        assertEquals(expectedBookings, result);
        verify(bookingService).findByOwnerId(userId, state);
    }

    @Test
    void findByOwnerId_withDefaultState_shouldUseAllState() {
        // Arrange
        int userId = 1;

        BookingDto booking = new BookingDto();
        booking.setId(1);
        List<BookingDto> expectedBookings = List.of(booking);

        when(bookingService.findByOwnerId(userId, BookingState.ALL)).thenReturn(expectedBookings);

        // Act
        List<BookingDto> result = bookingController.findByOwnerId(userId, BookingState.ALL);

        // Assert
        assertEquals(expectedBookings, result);
        verify(bookingService).findByOwnerId(userId, BookingState.ALL);
    }
}