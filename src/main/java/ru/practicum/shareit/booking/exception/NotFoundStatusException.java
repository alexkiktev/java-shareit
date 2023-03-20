package ru.practicum.shareit.booking.exception;

import org.springframework.http.HttpStatus;

public class NotFoundStatusException extends RuntimeException {

    public NotFoundStatusException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}
