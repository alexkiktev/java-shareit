package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class BookingOutputApprovedDto {
    private Long id;
    @NotNull
    private Item item;
    private User booker;
    private Status status;
}
