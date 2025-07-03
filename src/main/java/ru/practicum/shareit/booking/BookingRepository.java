package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByBookerIdOrderByStartDesc(Integer bookerId);

    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(Integer bookerId, LocalDateTime end);

    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(Integer bookerId, LocalDateTime start);

    List<Booking> findByBookerIdAndEndAfterAndStartBeforeOrderByStartDesc(Integer bookerId, LocalDateTime end, LocalDateTime start);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Integer bookerId, BookingStatus status);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Integer itemOwnerId);

    List<Booking> findByItemOwnerIdAndEndBeforeOrderByStartDesc(Integer itemOwnerId, LocalDateTime end);

    List<Booking> findByItemOwnerIdAndStartAfterOrderByStartDesc(Integer itemOwnerId, LocalDateTime start);

    List<Booking> findByItemOwnerIdAndEndAfterAndStartBeforeOrderByStartDesc(Integer itemOwnerId, LocalDateTime end, LocalDateTime start);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Integer itemOwnerId, BookingStatus status);


}
