package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findBookingsByBookerIdOrderByStartDesc(Long userId, Pageable pageParams);

    Page<Booking> findBookingByItem_OwnerOrderByStartDesc(Long ownerId, Pageable pageParams);

    @Query(value = "select count(*)  " +
            "from bookings as b " +
            "where b.item_id = ?1  " +
            "and b.booker_id = ?2  " +
            "and b.status = 'APPROVED'  " +
            "and b.end_date < now()", nativeQuery = true)
    Long findBookingsByItemIdAndBookerIdAndComplete(Long itemId, Long userId);

    List<Booking> findBookingsByItemIdAndStatus(Long id, Status approved);

}
