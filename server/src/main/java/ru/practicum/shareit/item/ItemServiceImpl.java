package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingInfo;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository,
                           CommentRepository commentRepository, BookingRepository bookingRepository,
                           ItemRequestRepository itemRequestRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.commentRepository = commentRepository;
        this.bookingRepository = bookingRepository;
        this.itemRequestRepository = itemRequestRepository;
    }

    @Override
    public Collection<ItemDtoWithBookingsInfo> findItemsByUserId(int userId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("Пользователь с id " + userId + " не найден."));
        List<Item> userItems = itemRepository.findAllWithCommentsByOwner(userId);

        List<ItemDtoWithBookingsInfo> result = new ArrayList<>();

        for (Item item : userItems) {
            BookingInfo lastBooking = null;
            BookingInfo nextBooking = null;
            // Получаем последнее завершенное бронирование
            Optional<Booking> lastBookingOpt = bookingRepository.findFirstByItemIdAndEndBeforeOrderByStartDesc(
                    item.getId(), LocalDateTime.now());

            // Получаем следующее бронирование
            Optional<Booking> nextBookingOpt = bookingRepository.findFirstByItemIdAndEndBeforeOrderByStartDesc(
                    item.getId(), LocalDateTime.now());

            if (lastBookingOpt.isPresent()) {
                Booking booking = lastBookingOpt.get();
                lastBooking = new BookingInfo(booking.getStart(), booking.getEnd());
            }

            if (nextBookingOpt.isPresent()) {
                Booking booking = nextBookingOpt.get();
                nextBooking = new BookingInfo(booking.getStart(), booking.getEnd());
            }
            result.add(ItemMapper.toItemDtoWithBookingInfo(item, lastBooking, nextBooking));
        }
        return result;
    }

    @Override
    public ItemDto create(int userId, NewItemRequest request) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("Пользователь с id " + userId + " не найден."));
        ItemRequest itemRequest = null;
        if (request.getRequestId() != null) {
            itemRequest = itemRequestRepository.findById(request.getRequestId()).orElseThrow(()
                    -> new NotFoundException("Запрос вещи с id " + userId + " не найден."));
        }
        Item item = ItemMapper.mapToItem(request, itemRequest, user);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto update(int userId, int itemId, ItemUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("Пользователь с id " + userId + " не найден."));
        Item item = itemRepository.findById(itemId).orElseThrow(()
        -> new NotFoundException("Вещь с id " + itemId + " не найдена."));
        item = ItemMapper.updateItemFields(item, request);
        Item newItem = itemRepository.save(item);
        return ItemMapper.toItemDto(newItem);
    }

    @Override
    public ItemDtoWithBookingsInfo findItemById(int itemId) {
        Item item = itemRepository.findOneWithComments(itemId).orElseThrow(()
                -> new NotFoundException("Вещь с id " + itemId + " не найдена."));
        BookingInfo lastBooking = null;
        BookingInfo nextBooking = null;
        return ItemMapper.toItemDtoWithBookingInfo(item, lastBooking, nextBooking);
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        if (text.isEmpty()) {
            return List.of();
        }
        return itemRepository.searchItems(text).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public CommentDto createComment(int userId, int itemId, NewCommentRequest request) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("Пользователь с id " + userId + " не найден."));
        Item item = itemRepository.findById(itemId).orElseThrow(()
                -> new NotFoundException("Вещь с id " + itemId + " не найдена."));
        if (bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now()).isEmpty()) {
            throw new ValidationException("Пользователь с id " + userId + " не брал вещь в аренду.");
        }
        Comment comment = CommentMapper.mapToComment(request,user, item);
        return CommentMapper.mapToCommentDto(commentRepository.save(comment));
    }
}
