package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingOutputDto {
    private Long id;
    @FutureOrPresent
    @NotNull
    private LocalDateTime start;
    @Future
    @NotNull
    private LocalDateTime end;
    @NotNull
    private Item item;
    private User booker;
    private Status status;
}