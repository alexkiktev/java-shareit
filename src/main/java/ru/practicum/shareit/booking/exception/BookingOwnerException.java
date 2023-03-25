package ru.practicum.shareit.booking.exception;

import org.springframework.http.HttpStatus;

public class BookingOwnerException extends RuntimeException {
    public BookingOwnerException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }

}
