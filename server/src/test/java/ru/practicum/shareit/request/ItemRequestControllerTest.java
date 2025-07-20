package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {

    @Mock
    private ItemRequestService itemRequestService;

    @InjectMocks
    private ItemRequestController itemRequestController;

    @Test
    void create_shouldCreateNewRequest() {
        // Arrange
        int userId = 1;
        NewItemRequestDto requestDto = new NewItemRequestDto();
        requestDto.setDescription("Need a drill");

        ItemRequestDto expectedResponse = new ItemRequestDto();
        expectedResponse.setId(1);
        expectedResponse.setDescription("Need a drill");

        when(itemRequestService.create(userId, requestDto)).thenReturn(expectedResponse);

        // Act
        ItemRequestDto result = itemRequestController.create(userId, requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(itemRequestService).create(userId, requestDto);
    }

    @Test
    void getRequestsByUser_shouldReturnUserRequests() {
        // Arrange
        int userId = 1;
        ItemRequestDto request1 = new ItemRequestDto();
        request1.setId(1);
        ItemRequestDto request2 = new ItemRequestDto();
        request2.setId(2);
        List<ItemRequestDto> expectedRequests = List.of(request1, request2);

        when(itemRequestService.getRequestsByUser(userId)).thenReturn(expectedRequests);

        // Act
        List<ItemRequestDto> result = itemRequestController.getRequestsByUser(userId);

        // Assert
        assertEquals(2, result.size());
        assertEquals(expectedRequests, result);
        verify(itemRequestService).getRequestsByUser(userId);
    }

    @Test
    void getRequestsByUser_whenNoRequests_shouldReturnEmptyList() {
        // Arrange
        int userId = 1;
        when(itemRequestService.getRequestsByUser(userId)).thenReturn(List.of());

        // Act
        List<ItemRequestDto> result = itemRequestController.getRequestsByUser(userId);

        // Assert
        assertTrue(result.isEmpty());
        verify(itemRequestService).getRequestsByUser(userId);
    }

    @Test
    void getAll_shouldReturnAllRequests() {
        // Arrange
        ItemRequestDto request1 = new ItemRequestDto();
        request1.setId(1);
        ItemRequestDto request2 = new ItemRequestDto();
        request2.setId(2);
        List<ItemRequestDto> expectedRequests = List.of(request1, request2);

        when(itemRequestService.getAll()).thenReturn(expectedRequests);

        // Act
        List<ItemRequestDto> result = itemRequestController.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals(expectedRequests, result);
        verify(itemRequestService).getAll();
    }

    @Test
    void getAll_whenNoRequests_shouldReturnEmptyList() {
        // Arrange
        when(itemRequestService.getAll()).thenReturn(List.of());

        // Act
        List<ItemRequestDto> result = itemRequestController.getAll();

        // Assert
        assertTrue(result.isEmpty());
        verify(itemRequestService).getAll();
    }

    @Test
    void getById_shouldReturnRequest() {
        // Arrange
        int requestId = 1;
        ItemRequestDto expectedRequest = new ItemRequestDto();
        expectedRequest.setId(requestId);
        expectedRequest.setDescription("Need a saw");

        when(itemRequestService.getById(requestId)).thenReturn(expectedRequest);

        // Act
        ItemRequestDto result = itemRequestController.getById(requestId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedRequest, result);
        verify(itemRequestService).getById(requestId);
    }

    @Test
    void getById_withNonExistingId_shouldReturnNull() {
        // Arrange
        int nonExistingId = 999;
        when(itemRequestService.getById(nonExistingId)).thenReturn(null);

        // Act
        ItemRequestDto result = itemRequestController.getById(nonExistingId);

        // Assert
        assertNull(result);
        verify(itemRequestService).getById(nonExistingId);
    }
}