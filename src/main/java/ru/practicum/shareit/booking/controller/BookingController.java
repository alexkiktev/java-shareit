package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingOutputDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingService bookingService;
    public static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public BookingOutputDto createBooking(@RequestHeader(USER_ID) Long bookerId,
                                          @RequestBody @Valid BookingInputDto bookingInputDto) {
        return bookingService.createBooking(bookerId, bookingInputDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingOutputDto approvedBooking(@RequestHeader(USER_ID) Long userId,
                                            @PathVariable Long bookingId,
                                            @RequestParam boolean approved) {
        return bookingService.approvedBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingOutputDto getBookingById(@RequestHeader(USER_ID) Long userId,
                                           @PathVariable @NotNull Long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingOutputDto> getBookingByUser(@RequestHeader(USER_ID) Long userId,
                                                   @RequestParam(defaultValue = "ALL") State state,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                       Integer from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "20")
                                                       Integer size) {
        return bookingService.getBookingByUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingOutputDto> getBookingByOwner(@RequestHeader(USER_ID) Long ownerId,
                                                    @RequestParam(defaultValue = "ALL") State state,
                                                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                        Integer from,
                                                    @Positive @RequestParam(name = "size", defaultValue = "20")
                                                        Integer size) {
        return bookingService.getBookingByOwner(ownerId, state, from, size);
    }

}
