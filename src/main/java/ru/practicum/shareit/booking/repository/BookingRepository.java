package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBookingsByBookerIdOrderByStartDesc(Long userId);

    List<Booking> findBookingByItem_OwnerOrderByStartDesc(Long ownerId);

    @Query(value = "select * " +
            "from bookings as b " +
            "where b.item_id = ?1 " +
            "and b.status = 'APPROVED'", nativeQuery = true)
    List<Booking> findBookingsByItemId(Long itemId);

    @Query(value = "select count(*)  " +
            "from bookings as b " +
            "where b.item_id = ?1  " +
            "and b.booker_id = ?2  " +
            "and b.status = 'APPROVED'  " +
            "and b.end_date < now()", nativeQuery = true)
    Long findBookingsByItemIdAndBookerIdAndComplete(Long itemId, Long userId);

}
