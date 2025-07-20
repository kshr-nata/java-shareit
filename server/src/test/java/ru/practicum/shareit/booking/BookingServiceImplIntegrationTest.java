package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BookingServiceImplIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User owner;
    private User booker;
    private Item item;

    @BeforeEach
    void setUp() {
        // Создаем владельца
        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@example.com");
        owner = userRepository.save(owner);

        // Создаем арендатора
        booker = new User();
        booker.setName("Booker");
        booker.setEmail("booker@example.com");
        booker = userRepository.save(booker);

        // Создаем вещь
        item = new Item();
        item.setName("Дрель");
        item.setDescription("Аккумуляторная дрель");
        item.setAvailable(true);
        item.setOwner(owner);
        item = itemRepository.save(item);
    }

    @Test
    void findByBookerId_whenStateAll_shouldReturnAllBookings() {
        // Создаем бронирования
        Booking pastBooking = createBooking(LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1));
        Booking currentBooking = createBooking(LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1));
        Booking futureBooking = createBooking(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));

        // Вызываем тестируемый метод
        List<BookingDto> result = bookingService.findByBookerId(booker.getId(), BookingState.ALL);

        // Проверяем результаты
        assertEquals(3, result.size(), "Должны вернуться все бронирования");
        assertEquals(futureBooking.getId(), result.get(0).getId(), "Первым должен быть самый поздний booking");
        assertEquals(currentBooking.getId(), result.get(1).getId());
        assertEquals(pastBooking.getId(), result.get(2).getId(), "Последним должен быть самый ранний booking");
    }

    @Test
    void findByBookerId_whenStateCurrent_shouldReturnCurrentBookings() {
        // Создаем бронирования
        createBooking(LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1)); // Прошедшее
        Booking currentBooking = createBooking(LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1)); // Текущее
        createBooking(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)); // Будущее

        // Вызываем тестируемый метод
        List<BookingDto> result = bookingService.findByBookerId(booker.getId(), BookingState.CURRENT);

        // Проверяем результаты
        assertEquals(1, result.size(), "Должно вернуться только текущее бронирование");
        assertEquals(currentBooking.getId(), result.get(0).getId());
    }

    @Test
    void findByBookerId_whenStatePast_shouldReturnPastBookings() {
        // Создаем бронирования
        Booking pastBooking = createBooking(LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1)); // Прошедшее
        createBooking(LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1)); // Текущее
        createBooking(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)); // Будущее

        // Вызываем тестируемый метод
        List<BookingDto> result = bookingService.findByBookerId(booker.getId(), BookingState.PAST);

        // Проверяем результаты
        assertEquals(1, result.size(), "Должно вернуться только прошедшее бронирование");
        assertEquals(pastBooking.getId(), result.get(0).getId());
    }

    @Test
    void findByBookerId_whenStateFuture_shouldReturnFutureBookings() {
        // Создаем бронирования
        createBooking(LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1)); // Прошедшее
        createBooking(LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1)); // Текущее
        Booking futureBooking = createBooking(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)); // Будущее

        // Вызываем тестируемый метод
        List<BookingDto> result = bookingService.findByBookerId(booker.getId(), BookingState.FUTURE);

        // Проверяем результаты
        assertEquals(1, result.size(), "Должно вернуться только будущее бронирование");
        assertEquals(futureBooking.getId(), result.get(0).getId());
    }

    @Test
    void findByBookerId_whenStateWaiting_shouldReturnWaitingBookings() {
        // Создаем бронирования с разными статусами
        Booking waitingBooking = createBooking(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), BookingStatus.WAITING);
        createBooking(LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4), BookingStatus.APPROVED);

        // Вызываем тестируемый метод
        List<BookingDto> result = bookingService.findByBookerId(booker.getId(), BookingState.WAITING);

        // Проверяем результаты
        assertEquals(1, result.size(), "Должно вернуться только бронирование со статусом WAITING");
        assertEquals(waitingBooking.getId(), result.get(0).getId());
    }

    @Test
    void findByBookerId_whenStateRejected_shouldReturnRejectedBookings() {
        // Создаем бронирования с разными статусами
        Booking rejectedBooking = createBooking(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), BookingStatus.REJECTED);
        createBooking(LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4), BookingStatus.APPROVED);

        // Вызываем тестируемый метод
        List<BookingDto> result = bookingService.findByBookerId(booker.getId(), BookingState.REJECTED);

        // Проверяем результаты
        assertEquals(1, result.size(), "Должно вернуться только бронирование со статусом REJECTED");
        assertEquals(rejectedBooking.getId(), result.get(0).getId());
    }

    @Test
    void findByBookerId_whenNoBookings_shouldReturnEmptyList() {
        // Вызываем тестируемый метод для пользователя без бронирований
        List<BookingDto> result = bookingService.findByBookerId(booker.getId(), BookingState.ALL);

        // Проверяем результаты
        assertTrue(result.isEmpty(), "Должен вернуться пустой список");
    }

    @Test
    void findByBookerId_whenUserNotFound_shouldThrowNotFoundException() {
        // Проверяем, что выбрасывается исключение для несуществующего пользователя
        assertThrows(NotFoundException.class, () -> {
            bookingService.findByBookerId(999, BookingState.ALL);
        }, "Должно быть выброшено NotFoundException");
    }

    private Booking createBooking(LocalDateTime start, LocalDateTime end) {
        return createBooking(start, end, BookingStatus.APPROVED);
    }

    private Booking createBooking(LocalDateTime start, LocalDateTime end, BookingStatus status) {
        Booking booking = new Booking();
        booking.setStart(start);
        booking.setEnd(end);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }
}