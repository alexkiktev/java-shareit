package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.exception.BookingOwnerException;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplMockTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingMapper bookingMapper;
    @InjectMocks
    private BookingServiceImpl bookingServiceImpl;

    private LocalDateTime timeStart = LocalDateTime.now().plusHours(2);
    private LocalDateTime timeEnd = LocalDateTime.now().plusHours(18);

    private User user1;
    private Item item1;
    private Booking booking1;
    private BookingInputDto bookingInputDto1;

    @BeforeEach
    void setUp() {
        timeStart = LocalDateTime.now().plusHours(2);
        timeEnd = LocalDateTime.now().plusHours(18);
        user1 = new User(2L, "nikolay@mail.ru", "Nikolay");
        item1 = new Item(1L, "Вещь 1", "Описание 1", true, 1L, null);
        booking1 = new Booking(1L, timeStart, timeEnd, item1, user1, null);
        bookingInputDto1 = new BookingInputDto(1L, timeStart, timeEnd);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void throwException_whenNotOwner_approvedBookingTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user1));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking1));

        Assertions.assertThrows(BookingOwnerException.class, () ->
                bookingServiceImpl.approvedBooking(user1.getId(), 1L, true));
    }

}