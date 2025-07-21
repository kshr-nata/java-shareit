package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemRequestServiceImplIntegrationTest {

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void getRequestsByUser_shouldReturnRequestsWithItems() {
        // Создаем пользователя
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user = userRepository.save(user);

        // Создаем запросы на вещи
        ItemRequest request1 = new ItemRequest();
        request1.setDescription("Нужна дрель");
        request1.setRequestor(user);
        request1.setCreated(LocalDateTime.now().minusDays(1));
        request1 = itemRequestRepository.save(request1);

        ItemRequest request2 = new ItemRequest();
        request2.setDescription("Нужен молоток");
        request2.setRequestor(user);
        request2.setCreated(LocalDateTime.now());
        request2 = itemRequestRepository.save(request2);

        // Создаем вещи для запросов
        Item item1 = new Item();
        item1.setName("Дрель");
        item1.setDescription("Аккумуляторная дрель");
        item1.setAvailable(true);
        item1.setOwner(user);
        item1.setRequest(request1);
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setName("Молоток");
        item2.setDescription("Строительный молоток");
        item2.setAvailable(true);
        item2.setOwner(user);
        item2.setRequest(request2);
        itemRepository.save(item2);

        // Вызываем тестируемый метод
        List<ItemRequestDto> result = itemRequestService.getRequestsByUser(user.getId());

        // Проверяем результаты
        assertEquals(2, result.size(), "Должно вернуться 2 запроса");

        // Проверяем первый запрос (должен быть вторым в списке, так как сортировка по дате DESC)
        ItemRequestDto firstRequest = result.get(0);
        assertEquals(request2.getId(), firstRequest.getId());
        assertEquals("Нужен молоток", firstRequest.getDescription());
        assertEquals(1, firstRequest.getItems().size());
        ItemDto firstItem = firstRequest.getItems().iterator().next();
        assertEquals("Молоток", firstItem.getName());

        // Проверяем второй запрос
        ItemRequestDto secondRequest = result.get(1);
        assertEquals(request1.getId(), secondRequest.getId());
        assertEquals("Нужна дрель", secondRequest.getDescription());
        assertEquals(1, secondRequest.getItems().size());
        ItemDto secondItem = secondRequest.getItems().iterator().next();
        assertEquals("Дрель", secondItem.getName());
    }

    @Test
    void getRequestsByUser_whenNoRequests_shouldReturnEmptyList() {
        // Создаем пользователя
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user = userRepository.save(user);

        // Вызываем тестируемый метод
        List<ItemRequestDto> result = itemRequestService.getRequestsByUser(user.getId());

        // Проверяем результаты
        assertTrue(result.isEmpty(), "Должен вернуться пустой список");
    }

    @Test
    void getRequestsByUser_whenUserNotFound_shouldThrowException() {
        // Вызываем тестируемый метод с несуществующим ID
        assertThrows(NotFoundException.class, () -> {
            itemRequestService.getRequestsByUser(999);
        }, "Должно быть выброшено NotFoundException");
    }
}