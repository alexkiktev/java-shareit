package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Assertions;
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

    LocalDateTime timeStart = LocalDateTime.now().plusHours(2);
    LocalDateTime timeEnd = LocalDateTime.now().plusHours(18);

    User user1 = new User(1L, "alex@mail.ru", "Alex");
    User user2 = new User(2L, "nikolay@mail.ru", "Nikolay");
    Item item1 = new Item(1L, "Вещь 1", "Описание 1", true, 1L, null);
    Item item2 = new Item(1L, "Вещь 2", "Описание 2", true, 2L, null);
    Booking booking1 = new Booking(1L, timeStart, timeEnd, item1, user2, null);
    BookingInputDto bookingInputDto1 = new BookingInputDto(1L, timeStart, timeEnd, 1L, 2L, null);

    @Test
    void throwException_whenNotOwner_approvedBookingTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user2));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking1));

        Assertions.assertThrows(BookingOwnerException.class, () ->
                bookingServiceImpl.approvedBooking(user2.getId(), bookingInputDto1.getId(), true));
    }

}