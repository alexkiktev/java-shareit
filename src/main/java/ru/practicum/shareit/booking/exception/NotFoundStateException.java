package ru.practicum.shareit.booking.exception;

import org.springframework.http.HttpStatus;

public class NotFoundStateException extends RuntimeException {

    public NotFoundStateException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}
