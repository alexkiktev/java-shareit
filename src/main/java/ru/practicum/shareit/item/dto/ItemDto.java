package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.utils.MarkerValidation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ItemDto {
    private Long id;
    @NotBlank(groups = {MarkerValidation.OnCreate.class})
    private String name;
    @NotBlank(groups = {MarkerValidation.OnCreate.class})
    private String description;
    @NotNull(groups = {MarkerValidation.OnCreate.class})
    private Boolean available;
}
