package ru.practicum.shareit.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.exception.dto.ErrorDto;
import ru.practicum.shareit.item.exception.CommentValidationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundStatusException.class)
    public ResponseEntity<ErrorDto> generateNotFoundStatusException(NotFoundStatusException ex) {
        ErrorDto errorDTO = new ErrorDto();
        errorDTO.setError(ex.getMessage());
        errorDTO.setStatus(String.valueOf(ex.getStatus().value()));
        return new ResponseEntity<>(errorDTO, ex.getStatus());
    }

    @ExceptionHandler(IncorrectDateTimeException.class)
    public ResponseEntity<ErrorDto> generateIncorrectDateTimeException(IncorrectDateTimeException ex) {
        ErrorDto errorDTO = new ErrorDto();
        errorDTO.setError(ex.getMessage());
        errorDTO.setStatus(String.valueOf(ex.getStatus().value()));
        return new ResponseEntity<>(errorDTO, ex.getStatus());
    }

    @ExceptionHandler(BookingOwnerException.class)
    public ResponseEntity<ErrorDto> generateBookingOwnerException(BookingOwnerException ex) {
        ErrorDto errorDTO = new ErrorDto();
        errorDTO.setError(ex.getMessage());
        errorDTO.setStatus(String.valueOf(ex.getStatus().value()));
        return new ResponseEntity<>(errorDTO, ex.getStatus());
    }

    @ExceptionHandler(BookingValidatedException.class)
    public ResponseEntity<ErrorDto> generateBookingValidatedException(BookingValidatedException ex) {
        ErrorDto errorDTO = new ErrorDto();
        errorDTO.setError(ex.getMessage());
        errorDTO.setStatus(String.valueOf(ex.getStatus().value()));
        return new ResponseEntity<>(errorDTO, ex.getStatus());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> generateNotFoundException(NotFoundException ex) {
        ErrorDto errorDTO = new ErrorDto();
        errorDTO.setError(ex.getMessage());
        errorDTO.setStatus(String.valueOf(ex.getStatus().value()));
        return new ResponseEntity<>(errorDTO, ex.getStatus());
    }

    @ExceptionHandler(CommentValidationException.class)
    public ResponseEntity<ErrorDto> generateCommentValidationException(CommentValidationException ex) {
        ErrorDto errorDTO = new ErrorDto();
        errorDTO.setError(ex.getMessage());
        errorDTO.setStatus(String.valueOf(ex.getStatus().value()));
        return new ResponseEntity<>(errorDTO, ex.getStatus());
    }

}
