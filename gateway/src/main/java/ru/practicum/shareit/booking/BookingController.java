package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingInputDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingClient bookingClient;
    public static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader(USER_ID) Long bookerId,
                                          @RequestBody @Valid BookingInputDto bookingInputDto) {
        log.info("Создание брони {}, bookerId={}", bookingInputDto, bookerId);
        if (bookerId == null) {
            bookerId = 1L;
        }
        return bookingClient.createBooking(bookerId, bookingInputDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approvedBooking(@RequestHeader(USER_ID) Long userId,
                                                  @PathVariable Long bookingId,
                                                  @RequestParam boolean approved) {
        return bookingClient.approvedBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(USER_ID) Long userId,
                                           @PathVariable @NotNull Long bookingId) {
        return bookingClient.getBookingById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingByUser(@RequestHeader(USER_ID) Long userId,
                                                   @RequestParam(defaultValue = "ALL") String state,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                       Integer from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "20")
                                                       Integer size) {
        return bookingClient.getBookingByUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingByOwner(@RequestHeader(USER_ID) Long ownerId,
                                                    @RequestParam(defaultValue = "ALL") String state,
                                                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                        Integer from,
                                                    @Positive @RequestParam(name = "size", defaultValue = "20")
                                                        Integer size) {
        return bookingClient.getBookingByOwner(ownerId, state, from, size);
    }

}
