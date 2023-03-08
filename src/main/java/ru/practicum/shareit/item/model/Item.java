package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class Item {
    private Long id;
    @NotEmpty
    private String name;
    private String description;
    private Boolean available;
    private Long owner;
    private Long itemRequest;
}