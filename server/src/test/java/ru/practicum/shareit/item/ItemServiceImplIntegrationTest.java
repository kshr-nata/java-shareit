package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingInfo;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingsInfo;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceImplIntegrationTest {

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CommentRepository commentRepository;

    private User owner;
    private User booker;
    private Item item1;
    private Item item2;
    private Booking pastBooking;
    private Booking futureBooking;
    private Comment comment;

    @BeforeEach
    void setUp() {
        owner = userRepository.save(new User(null, "Owner", "owner@email.com"));
        booker = userRepository.save(new User(null, "Booker", "booker@email.com"));

        item1 = itemRepository.save(new Item(null, "Дрель", "Простая дрель", true, owner, null, new HashSet<>()));
        item2 = itemRepository.save(new Item(null, "Отвертка", "Крестовая отвертка", true, owner, null, new HashSet<>()));

        LocalDateTime now = LocalDateTime.now();
        pastBooking = bookingRepository.save(
                new Booking(null, now.minusDays(2), now.minusDays(1), item1, booker, BookingStatus.APPROVED));
        futureBooking = bookingRepository.save(
                new Booking(null, now.plusDays(1), now.plusDays(2), item1, booker, BookingStatus.APPROVED));

        comment = commentRepository.save(
                new Comment(null, "Хорошая дрель", item1, booker, now));

        item1.getComments().add(comment);
        itemRepository.save(item1);
    }

    @Test
    void findItemsByUserId_shouldReturnItemsWithBookingsAndComments() {
        Collection<ItemDtoWithBookingsInfo> result = itemService.findItemsByUserId(owner.getId());

        assertNotNull(result);
        assertEquals(2, result.size());

        ItemDtoWithBookingsInfo itemWithBookings = findItemByName(result, "Дрель");
        assertBookingInfo(itemWithBookings.getLastBooking(), pastBooking);
        assertBookingInfo(itemWithBookings.getNextBooking(), futureBooking);
        assertComments(itemWithBookings.getComments(), "Хорошая дрель");

        ItemDtoWithBookingsInfo itemWithoutBookings = findItemByName(result, "Отвертка");
        assertNull(itemWithoutBookings.getLastBooking());
        assertNull(itemWithoutBookings.getNextBooking());
        assertTrue(itemWithoutBookings.getComments().isEmpty());
    }

    private ItemDtoWithBookingsInfo findItemByName(Collection<ItemDtoWithBookingsInfo> items, String name) {
        return items.stream()
                .filter(i -> i.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Item not found: " + name));
    }

    private void assertBookingInfo(BookingInfo actual, Booking expected) {
        assertNotNull(actual);
        assertEquals(expected.getStart(), actual.getStart());
        assertEquals(expected.getEnd(), actual.getEnd());
    }

    private void assertComments(Set<String> comments, String... expectedComments) {
        assertEquals(expectedComments.length, comments.size());
        assertTrue(comments.containsAll(Arrays.asList(expectedComments)));
    }

    @Test
    void findItemsByUserId_whenUserNotFound_shouldThrowException() {
        assertThrows(NotFoundException.class,
                () -> itemService.findItemsByUserId(999));
    }
}