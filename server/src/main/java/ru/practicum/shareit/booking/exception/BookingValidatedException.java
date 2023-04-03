package ru.practicum.shareit.booking.exception;

import org.springframework.http.HttpStatus;

public class BookingValidatedException extends RuntimeException {
    public BookingValidatedException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}