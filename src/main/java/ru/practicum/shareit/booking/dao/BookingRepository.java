package ru.practicum.shareit.booking.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "where i.id = ?1 and b.start < ?2 and b.status = 'APPROVED'" +
            "order by b.start desc")
    List<Booking> findLastBookingByItemId(Long itemId, LocalDateTime dateTime);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "where i.id = ?1 and b.start > ?2 and b.status = 'APPROVED'" +
            "order by b.start")
    List<Booking> findNextBookingByItemId(Long itemId, LocalDateTime dateTime);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i join i.owner as u " +
            "where u.id = ?1 and b.start < ?2 " +
            "order by b.start desc")
    List<Booking> findLastBookingsByBookerId(Long userId, LocalDateTime dateTime);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i join i.owner as u " +
            "where u.id = ?1 and b.start > ?2 " +
            "order by b.start")
    List<Booking> findNextBookingsByBookerId(Long userId, LocalDateTime dateTime);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join b.booker as bo " +
            "where bo.id = ?1 and i.id = ?2 and b.status = 'APPROVED' and b.start < ?3 and b.end < ?3")
    List<Booking> findAllByBookerIdAndItemId(Long userId, Long itemId, LocalDateTime dateTime);

    Page<Booking> findAllByItemOwnerIdOrderByStartDesc(long userId, Pageable page);

    Page<Booking> findAllByBookerIdOrderByStartDesc(long userId, Pageable page);

    Page<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStart(long userId, LocalDateTime start,
                                                                         LocalDateTime end, Pageable page);


    Page<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(long userId, LocalDateTime now, Pageable page);

    Page<Booking> findAllByBookerIdAndStatusOrderByStartDesc(long userId, Status status, Pageable page);

    Page<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStart(long userId, LocalDateTime start,
                                                                            LocalDateTime end, Pageable page);

    Page<Booking> findAllByItemOwnerIdAndEndBeforeAndStatusOrderByStartDesc(long userId, LocalDateTime now,
                                                                            Status status, Pageable page);

    Page<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(long userId, LocalDateTime now, Pageable page);

    Page<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(long userId, Status dateStatus, Pageable page);

    Page<Booking> findAllByBookerIdAndEndBeforeAndStatusOrderByStartDesc(long userId, LocalDateTime now,
                                                                         Status status, Pageable page);
}
