package ru.practicum.shareit.booking.exception;

import org.springframework.http.HttpStatus;

public class IncorrectDateTimeException extends RuntimeException {
    public IncorrectDateTimeException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}