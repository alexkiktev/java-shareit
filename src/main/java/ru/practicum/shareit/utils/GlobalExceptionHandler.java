package ru.practicum.shareit.utils;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.exception.dto.ErrorDto;
import ru.practicum.shareit.item.exception.CommentValidationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundStateException.class)
    public ResponseEntity<ErrorDto> generateNotFoundStateException(NotFoundStateException ex) {
        ErrorDto errorDTO = new ErrorDto();
        errorDTO.setError(ex.getMessage());
        errorDTO.setStatus(String.valueOf(ex.getStatus().value()));
        return new ResponseEntity<>(errorDTO, ex.getStatus());
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<ErrorDto> handleConflict(RuntimeException ex) {
        ErrorDto errorDTO = new ErrorDto();
        errorDTO.setError("Unknown state: UNSUPPORTED_STATUS");
        errorDTO.setStatus("400");
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
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
