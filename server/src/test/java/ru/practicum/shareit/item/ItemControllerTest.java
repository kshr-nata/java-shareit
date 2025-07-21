package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    @Test
    void findItemsByUser_shouldReturnListOfItems() {
        // Arrange
        int userId = 1;
        ItemDtoWithBookingsInfo itemDto = new ItemDtoWithBookingsInfo();
        List<ItemDtoWithBookingsInfo> expectedItems = Collections.singletonList(itemDto);

        when(itemService.findItemsByUserId(userId)).thenReturn(expectedItems);

        // Act
        Collection<ItemDtoWithBookingsInfo> result = itemController.findItemsByUser(userId);

        // Assert
        assertEquals(expectedItems, result);
        verify(itemService).findItemsByUserId(userId);
    }

    @Test
    void create_shouldCreateNewItem() {
        // Arrange
        int userId = 1;
        NewItemRequest newItemRequest = new NewItemRequest();
        newItemRequest.setName("Test Item");
        newItemRequest.setDescription("Test Description");
        newItemRequest.setAvailable(true);

        ItemDto expectedItemDto = new ItemDto();
        expectedItemDto.setId(1);
        expectedItemDto.setName("Test Item");

        when(itemService.create(userId, newItemRequest)).thenReturn(expectedItemDto);

        // Act
        ItemDto result = itemController.create(userId, newItemRequest);

        // Assert
        assertEquals(expectedItemDto, result);
        verify(itemService).create(userId, newItemRequest);
    }

    @Test
    void update_shouldUpdateExistingItem() {
        // Arrange
        int userId = 1;
        int itemId = 1;
        ItemUpdateRequest updateRequest = new ItemUpdateRequest();
        updateRequest.setName("Updated Name");

        ItemDto expectedItemDto = new ItemDto();
        expectedItemDto.setId(itemId);
        expectedItemDto.setName("Updated Name");

        when(itemService.update(userId, itemId, updateRequest)).thenReturn(expectedItemDto);

        // Act
        ItemDto result = itemController.update(userId, itemId, updateRequest);

        // Assert
        assertEquals(expectedItemDto, result);
        verify(itemService).update(userId, itemId, updateRequest);
    }

    @Test
    void findById_shouldReturnItem() {
        // Arrange
        int itemId = 1;
        ItemDtoWithBookingsInfo expectedItem = new ItemDtoWithBookingsInfo();
        expectedItem.setId(itemId);

        when(itemService.findItemById(itemId)).thenReturn(expectedItem);

        // Act
        ItemDtoWithBookingsInfo result = itemController.findById(itemId);

        // Assert
        assertEquals(expectedItem, result);
        verify(itemService).findItemById(itemId);
    }

    @Test
    void searchItems_shouldReturnListOfAvailableItems() {
        // Arrange
        String searchText = "test";
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        List<ItemDto> expectedItems = Collections.singletonList(itemDto);

        when(itemService.searchItems(searchText)).thenReturn(expectedItems);

        // Act
        Collection<ItemDto> result = itemController.searchItems(searchText);

        // Assert
        assertEquals(expectedItems, result);
        verify(itemService).searchItems(searchText);
    }

    @Test
    void createComment_shouldCreateNewComment() {
        // Arrange
        int userId = 1;
        int itemId = 1;
        NewCommentRequest newCommentRequest = new NewCommentRequest();
        newCommentRequest.setText("Test comment");

        CommentDto expectedCommentDto = new CommentDto();
        expectedCommentDto.setText("Test comment");

        when(itemService.createComment(userId, itemId, newCommentRequest)).thenReturn(expectedCommentDto);

        // Act
        CommentDto result = itemController.createComment(userId, itemId, newCommentRequest);

        // Assert
        assertEquals(expectedCommentDto, result);
        verify(itemService).createComment(userId, itemId, newCommentRequest);
    }

}