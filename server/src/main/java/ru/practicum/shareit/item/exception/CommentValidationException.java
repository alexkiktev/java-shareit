package ru.practicum.shareit.item.exception;

import org.springframework.http.HttpStatus;

public class CommentValidationException extends RuntimeException {
    public CommentValidationException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}
