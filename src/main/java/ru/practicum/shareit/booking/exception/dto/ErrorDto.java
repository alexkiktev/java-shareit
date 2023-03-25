package ru.practicum.shareit.booking.exception.dto;

import lombok.Data;

@Data
public class ErrorDto {

    public String status;
    public String error;

}
