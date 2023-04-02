package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class BookingServiceImplTest {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookingServiceImpl bookingServiceImpl;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private ItemServiceImpl itemServiceImpl;
    @Autowired
    private ItemRepository itemRepository;

    @Test
    @Transactional
    @Sql("classpath:cleanup.sql")
    @Sql("classpath:test_users.sql")
    @Sql("classpath:test_items.sql")
    @Sql("classpath:test_bookings.sql")
    void givenBookingDto_whenUserIsCreator_getBookingById() {
        Long bookerId = 3L;
        Long bookingId = 1L;

        BookingOutputDto bookingOutputDto = bookingServiceImpl.getBookingById(bookerId, bookingId);

        Assertions.assertNotNull(bookingOutputDto);
    }

    @Test
    @Transactional
    @Sql("classpath:cleanup.sql")
    @Sql("classpath:test_users.sql")
    @Sql("classpath:test_items.sql")
    @Sql("classpath:test_bookings.sql")
    void givenBookingDtoList_whenUserIsCreator_getBookingByUser() {
        Long bookerId = 1L;

        List<BookingOutputDto> bookingOutputDto =
                bookingServiceImpl.getBookingByUser(bookerId, State.ALL, 0, 20);

        Assertions.assertTrue(bookingOutputDto.size() > 0);
    }

}