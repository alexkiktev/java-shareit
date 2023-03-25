package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingItemInfoDto;

import java.util.List;

@Getter
@Setter
@Builder
public class ItemOwnerDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    private BookingItemInfoDto lastBooking;
    private BookingItemInfoDto nextBooking;

    private List<CommentDto> comments;
}
