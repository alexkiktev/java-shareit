package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {

    BookingOutputDto createBooking(Long userId, BookingInputDto bookingInputDto);

    BookingOutputDto approvedBooking(Long userId, Long bookingId, boolean approved);

    BookingOutputDto getBookingById(Long userId, Long bookingId);

    List<BookingOutputDto> getBookingByUser(Long userId, State state);

    List<BookingOutputDto> getBookingByOwner(Long ownerId, State state);

}
