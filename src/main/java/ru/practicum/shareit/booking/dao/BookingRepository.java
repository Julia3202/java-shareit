package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerId(long userId);

    List<Booking> findByItemOwnerId(long userId);
    List<Booking> findAllByItemIdAndBookerIdAndStatusAndStartBeforeAndEndBefore(long itemId,
                                                                                  long userId, Status status,
                                                                                  LocalDateTime start,
                                                                                  LocalDateTime end);
}
