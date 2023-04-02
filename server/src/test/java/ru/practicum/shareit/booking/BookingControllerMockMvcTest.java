package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookingService bookingService;

    private Long userId;
    private User user;
    private Long itemId;
    private Item item;
    private Long bookingId;
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;
    private BookingInputDto bookingInputDto;
    private BookingOutputDto bookingOutputDto;
    private BookingOutputDto approvedBookingOutputDto;

    @BeforeEach
    void setUp() {
        userId = 1L;
        user = new User(userId, "test@test.ru", "Alex");
        itemId = 1L;
        item = new Item(itemId, "Отвертка", "Крестовая отвертка", true, userId, null);
        bookingId = 1L;
        dateStart = LocalDateTime.now().plusHours(1);
        dateEnd = LocalDateTime.now().plusHours(8);
        bookingInputDto = new BookingInputDto(bookingId, dateStart, dateEnd, itemId, null, null);
        bookingOutputDto = new BookingOutputDto(bookingId, dateStart, dateEnd, item, user, null);
        approvedBookingOutputDto = new BookingOutputDto(bookingId, dateStart, dateEnd, item, user, Status.APPROVED);
    }

    @Test
    void givenBookingOutputDto_whenSuccessful_createBookingTest() throws Exception {
        when(bookingService.createBooking(userId, bookingInputDto)).thenReturn(bookingOutputDto);

        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingInputDto))
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingOutputDto)));
    }

    @Test
    void throwException_whenItemIdIsNull_CreateItemTest() throws Exception {
        bookingInputDto.setItemId(null);

        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingInputDto))
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void throwException_whenStartIsNull_CreateItemTest() throws Exception {
        bookingInputDto.setStart(null);

        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingInputDto))
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void throwException_whenEndIsNull_CreateItemTest() throws Exception {
        bookingInputDto.setEnd(null);

        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingInputDto))
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void throwException_whenStartInPast_CreateItemTest() throws Exception {
        bookingInputDto.setStart(LocalDateTime.now().minusDays(1));

        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingInputDto))
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void throwException_whenEndInPast_CreateItemTest() throws Exception {
        bookingInputDto.setEnd(LocalDateTime.now().minusDays(1));

        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingInputDto))
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenBookingOutputDto_whenSuccessful_approvedBookingTest() throws Exception {
        when(bookingService.approvedBooking(userId, bookingId, true)).thenReturn(approvedBookingOutputDto);

        mockMvc.perform(patch("/bookings/" + bookingId + "?approved=true")
                        .content(objectMapper.writeValueAsString(bookingInputDto))
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(approvedBookingOutputDto.getId()), Long.class));

        verify(bookingService, times(1)).approvedBooking(userId, bookingId, true);
    }

    @Test
    void givenBookingOutputDto_whenSuccessful_getBookingByIdTest() throws Exception {
        when(bookingService.getBookingById(userId, bookingId)).thenReturn(approvedBookingOutputDto);

        mockMvc.perform(get("/bookings/" + bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(approvedBookingOutputDto.getId()), Long.class));

        verify(bookingService, times(1)).getBookingById(userId, bookingId);
    }

    @Test
    void throwException_whenUserNotFound_getBookingByIdTest() throws Exception {
        Long controlUserId = 9999999999L;
        when(bookingService.getBookingById(controlUserId, bookingId))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/bookings/" + bookingId)
                        .header("X-Sharer-User-Id", controlUserId))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenBookingOutputDtoList_whenSuccessful_getBookingByUserTest() throws Exception {
        State state = State.ALL;
        Integer from = 0;
        Integer size = 10;
        when(bookingService.getBookingByUser(userId, state, from, size)).thenReturn(List.of(approvedBookingOutputDto));

        mockMvc.perform(get("/bookings/?state=ALL&from=0&size=10")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).getBookingByUser(userId, state, from, size);
    }

    @Test
    void givenBookingOutputDtoList_whenSuccessful_getBookingByOwnerTest() throws Exception {
        State state = State.ALL;
        Integer from = 0;
        Integer size = 10;
        when(bookingService.getBookingByOwner(userId, state, from, size)).thenReturn(List.of(approvedBookingOutputDto));

        mockMvc.perform(get("/bookings/owner?state=ALL&from=0&size=10")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).getBookingByOwner(userId, state, from, size);
    }

}